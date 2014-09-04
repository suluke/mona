package de.lksbhm.mona.levels;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.tutorials.Tutorial;

/**
 * Levels always belong to a package, otherwise they would just be mere puzzles.
 *
 */
public abstract class Level extends ContextImplementation implements Disposable {

	private static final String tutorialPackage = "de.lksbhm.mona.tutorials";

	private final String id;
	private final LevelPackage pack;
	private final boolean solved = false;
	private Puzzle p;
	private Tutorial tutorial;

	public Level(LevelPackage pack, String id) {
		this.pack = pack;
		this.id = id;
	}

	public boolean isSolved() {
		return solved;
	}

	public Puzzle getPuzzle() {
		if (p == null) {
			p = instantiatePuzzle();
		}
		return p;
	}

	protected abstract Puzzle instantiatePuzzle();

	public String getLevelId() {
		return id;
	}

	public LevelPackage getPackage() {
		return pack;
	}

	public void reset() {
		if (p != null) {
			p.clearInOuDirections();
		}
	}

	@Override
	public void dispose() {
		if (p != null) {
			p.dispose();
			p = null;
		}
		if (tutorial != null) {
			tutorial.dispose();
		}
	}

	public Level getNextLevel() {
		return pack.getLevelAfter(this);
	}

	public void setTutorialByName(String name) {
		Class<?> classWithName;
		name = tutorialPackage + "." + name;
		try {
			classWithName = ClassReflection.forName(name);
		} catch (ReflectionException e) {
			throw new RuntimeException("It seems no tutorial exists with name "
					+ name);
		}
		if (!Tutorial.class.isAssignableFrom(classWithName)) {
			throw new RuntimeException();
		}
		@SuppressWarnings("unchecked")
		Class<? extends Tutorial> tutorialClass = (Class<? extends Tutorial>) classWithName;
		Constructor constructor;
		try {
			constructor = ClassReflection.getConstructor(tutorialClass,
					Level.class);
		} catch (ReflectionException e) {
			throw new RuntimeException();
		}
		Tutorial tut;
		try {
			tut = (Tutorial) constructor.newInstance(this);
		} catch (ReflectionException e) {
			throw new RuntimeException();
		}
		removeTutorial();
		tut.load();
		this.tutorial = tut;
	}

	public void removeTutorial() {
		if (tutorial != null) {
			tutorial.dispose();
			tutorial = null;
		}
	}
}
