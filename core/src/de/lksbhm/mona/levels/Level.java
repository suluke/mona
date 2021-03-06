package de.lksbhm.mona.levels;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;
import de.lksbhm.mona.tutorials.Tutorial;
import de.lksbhm.mona.ui.screens.AbstractLevelScreen;

/**
 * Levels always belong to a package, otherwise they would just be mere puzzles.
 *
 */
public abstract class Level extends ContextImplementation implements Disposable {

	public static final String packageIdSeparator = "/";
	private static final String tutorialPackage = "de.lksbhm.mona.tutorials";

	private final String id;
	private final LevelPackage pack;
	private LevelPuzzle p;
	private Tutorial tutorial;
	private final String canonicalId;
	private DirectionalTileBoard solution;

	private AbstractLevelScreen<?> view;

	public Level(LevelPackage pack, String id) {
		this.pack = pack;
		this.id = id;
		if (pack != null) {
			canonicalId = pack.getPackageId() + packageIdSeparator + id;
		} else {
			canonicalId = id;
		}
	}

	public boolean isSolved() {
		return Mona.getGame().getUserManager().getCurrentUser()
				.isLevelSolved(this);
	}

	public Puzzle getPuzzle() {
		if (p == null) {
			p = instantiatePuzzle();
		}
		return p;
	}

	public DirectionalTileBoard getSolution() {
		if (solution == null) {
			solution = instantiateSolution();
		}
		return solution;
	}

	protected abstract LevelPuzzle instantiatePuzzle();

	protected abstract DirectionalTileBoard instantiateSolution();

	public String getLevelId() {
		return id;
	}

	public LevelPackage getPackage() {
		return pack;
	}

	public void reset() {
		if (p != null) {
			p.reset();
		}
	}

	@Override
	public void dispose() {
		if (p != null) {
			p.dispose();
			p = null;
		}
		if (hasTutorial()) {
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
		if (!ClassReflection.isAssignableFrom(Tutorial.class, classWithName)) {
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
		if (constructor == null) {
			throw new RuntimeException("Tutorial not found: " + name);
		}
		Tutorial tut;
		try {
			tut = (Tutorial) constructor.newInstance(this);
		} catch (ReflectionException e) {
			throw new RuntimeException(e);
		}
		removeTutorial();
		tut.load();
		this.tutorial = tut;
	}

	public void removeTutorial() {
		if (hasTutorial()) {
			tutorial.dispose();
			tutorial = null;
		}
	}

	public boolean hasTutorial() {
		return tutorial != null;
	}

	public String getTutorialName() {
		if (hasTutorial()) {
			return tutorial.getCanonicalClassName().substring(
					tutorialPackage.length() + 1);
			// +1 because of additional '.'
		} else {
			throw new RuntimeException(
					"No tutorial has been set for this level");
		}
	}

	public String getCanonicalId() {
		return canonicalId;
	}

	public static String getPackageIdfromCanonicalId(String canonical) {
		int separatorIndex = canonical.indexOf(packageIdSeparator);
		if (separatorIndex == -1) {
			throw new RuntimeException(canonical);
		} else {
			return canonical.substring(0, separatorIndex);
		}
	}

	public void notifySolved() {
		if (!isSolved()) {
			Mona.getGame().getUserManager().getCurrentUser()
					.setLevelSolved(Level.this);
		}
	}

	public void setView(AbstractLevelScreen<?> view) {
		this.view = view;
	}

	public AbstractLevelScreen<?> getView() {
		return view;
	}

	public void applySolution() {
		Puzzle puzzle = getPuzzle();
		DirectionalTileBoard solution = getSolution();
		puzzle.applyDirections(solution, false);
	}

	/**
	 * 
	 * @param solution
	 * @deprecated only for testing purposes
	 */
	@Deprecated
	void setSolution(DirectionalTileBoard solution) {
		this.solution = solution;
	}
}
