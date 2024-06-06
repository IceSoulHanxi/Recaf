package me.coley.recaf.decompile.fernflower;

import me.coley.recaf.workspace.JavaResource;
import me.coley.recaf.workspace.Workspace;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.jetbrains.java.decompiler.struct.IDecompiledData;
import org.jetbrains.java.decompiler.struct.StructContext;

import java.io.IOException;

/**
 * Decorator for StructContext to support Recaf workspaces.
 *
 * @author Matt
 */
public class StructContextDecorator extends StructContext {
	/**
	 * Constructs a StructContext.
	 *
	 * @param saver
	 * 		Result saver <i>(Unused/noop)</i>
	 * @param data
	 * 		Data instance, should be an instance of
	 *        {@link me.coley.recaf.decompile.fernflower.FernFlowerAccessor}.
	 * @param legacyProvider
	 * 		LazyLoader to hold links to class resources.
	 */
	public StructContextDecorator(IResultSaver saver, IDecompiledData data, IBytecodeProvider legacyProvider) {
		super(legacyProvider, saver, data);
	}

	/**
	 * @param workspace
	 * 		Recaf workspace to pull classes from.
	 *
	 * @throws IOException
	 * 		Thrown if a class cannot be read.
	 * @throws IndexOutOfBoundsException
	 * 		Thrown if FernFlower can't read the class.
	 * 		<i>(IE: It fails on newer Java class files)</i>
	 */
	public void addWorkspace(Workspace workspace) throws IOException {
		// Add primary resource classes
		addResource(workspace.getPrimary());
		for (JavaResource resource : workspace.getLibraries())
			addResource(resource);
	}

	private void addResource(JavaResource resource) throws IOException {
		addSpace(new JavaResourceContextSource(resource), true);
	}
}
