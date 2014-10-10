/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.lksbhm.gdx.util;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

import com.badlogic.gdx.utils.Array;

/**
 * Builder style API for emitting JSON.
 * 
 * @author Nathan Sweet
 */
public class JsonWriter extends Writer {
	final Writer writer;
	private final Array<JsonObject> stack = new Array<JsonObject>();
	private JsonObject current;
	private boolean named;
	private OutputType outputType = OutputType.json;
	private boolean indentation = false;
	private boolean isNewLine = true;
	private String indentationSequence = "\t";

	public JsonWriter(Writer writer) {
		this.writer = writer;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}

	public void setIndentation(boolean indentation) {
		this.indentation = indentation;
	}

	public void setIndentationSequence(String indentationSequence) {
		this.indentationSequence = indentationSequence;
	}

	public JsonWriter name(String name) throws IOException {
		checkState(current == null || current.array,
				"Current item must be an object.");
		if (!current.needsComma) {
			current.needsComma = true;
		} else {
			writer.write(',');
			if (indentation) {
				newLine();
			}
		}
		if (indentation) {
			indent();
		}
		writer.write(outputType.quoteName(name));
		writer.write(':');
		if (indentation) {
			writer.write(' ');
		}
		named = true;
		return this;
	}

	public JsonWriter object() throws IOException {
		if (current != null) {
			if (current.array) {
				if (!current.needsComma) {
					current.needsComma = true;
				} else {
					writer.write(',');
					if (indentation) {
						newLine();
					}
				}
			} else {
				checkState(!named && !current.array, "Name must be set.");
				named = false;
			}
		}
		stack.add(current = new JsonObject(false));
		return this;
	}

	public JsonWriter array() throws IOException {
		if (current != null) {
			if (current.array) {
				if (!current.needsComma) {
					current.needsComma = true;
				} else {
					writer.write(',');
					if (indentation) {
						newLine();
					}
				}
			} else {
				checkState(!named && !current.array, "Name must be set.");
				named = false;
			}
		}
		stack.add(current = new JsonObject(true));
		return this;
	}

	public JsonWriter value(Object value) throws IOException {
		if (value instanceof Number) {
			Number number = (Number) value;
			long longValue = number.longValue();
			if (number.doubleValue() == longValue) {
				value = longValue;
			}
		}
		if (current != null) {
			if (current.array) {
				if (!current.needsComma) {
					current.needsComma = true;
				} else {
					writer.write(',');
					if (indentation) {
						writer.write(' ');
					}
				}
			} else {
				checkState(!named, "Name must be set.");
				named = false;
			}
		}
		if (indentation && isNewLine) {
			indent();
		}
		writer.write(outputType.quoteValue(value));
		isNewLine = false;
		return this;
	}

	public JsonWriter object(String name) throws IOException {
		return name(name).object();
	}

	public JsonWriter array(String name) throws IOException {
		return name(name).array();
	}

	public JsonWriter set(String name, Object value) throws IOException {
		return name(name).value(value);
	}

	public JsonWriter pop() throws IOException {
		checkState(named,
				"Expected an object, array, or value since a name was set.");
		stack.pop().close();
		current = stack.size == 0 ? null : stack.peek();
		return this;
	}

	private void indent() throws IOException {
		isNewLine = false;
		for (int i = 0; i < stack.size; i++) {
			writer.write(indentationSequence);
		}
	}

	private void newLine() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		writer.write(lineSeparator);
		isNewLine = true;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		while (stack.size > 0)
			pop();
		writer.close();
	}

	private void checkState(boolean condition, String explanation) {
		if (condition) {
			throw new IllegalStateException(explanation);
		}
	}

	private class JsonObject {
		final boolean array;
		boolean needsComma;

		JsonObject(boolean array) throws IOException {
			this.array = array;
			// only if in array, since there would already be indentation for
			// the name in an object
			if (current != null && current.array && indentation) {
				indent();
			}
			writer.write(array ? '[' : '{');
			if (indentation) {
				newLine();
			}
		}

		void close() throws IOException {
			if (indentation) {
				if (!isNewLine) {
					newLine();
				}
				indent();
			}
			writer.write(array ? ']' : '}');
		}
	}

	static public enum OutputType {
		/**
		 * Normal JSON, with all its quotes.
		 */
		json,
		/**
		 * Like JSON, but names are only quoted if necessary.
		 */
		javascript,
		/**
		 * Like JSON, but names and values are only quoted if they don't contain
		 * <code>\r\n\t</code> or <code>space</code> and don't begin with
		 * <code>{}[]:,"</code>. Additionally, names cannot contain
		 * <code>:</code> and values cannot contain <code>}],</code>.
		 */
		minimal;

		private static Pattern javascriptPattern = Pattern
				.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
		private static Pattern minimalNamePattern = Pattern
				.compile("[^{}\\[\\],\":\\r\\n\\t ][^:\\r\\n\\t ]*");
		private static Pattern minimalValuePattern = Pattern
				.compile("[^{}\\[\\],\":\\r\\n\\t ][^}\\],\\r\\n\\t ]*");

		private String quoteValue(Object value) {
			if (isPrimitive(value)) {
				return String.valueOf(value);
			} else {
				String string = escapeString(String.valueOf(value));
				if (this == OutputType.minimal && !isContentAmbiguous(string)
						&& minimalValuePattern.matcher(string).matches()) {
					return string;
				}
				return '"' + string + '"';
			}
		}

		private String quoteName(String value) {
			value = escapeString(value);
			switch (this) {
			case minimal:
				if (minimalNamePattern.matcher(value).matches())
					return value;
			case javascript:
				if (javascriptPattern.matcher(value).matches())
					return value;
			default:
				break;
			}
			return '"' + value + '"';
		}

		private static boolean isPrimitive(Object value) {
			return value == null || value instanceof Number
					|| value instanceof Boolean;
		}

		private static boolean isContentAmbiguous(String string) {
			return string.equals("true") || string.equals("false")
					|| string.equals("null");
		}

		private static String escapeString(String string) {
			return string.replace("\\", "\\\\").replace("\r", "\\r")
					.replace("\n", "\\n").replace("\t", "\\t")
					.replace("\"", "\\\"");
		}
	}
}
