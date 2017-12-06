package tp7.refactorings;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.MoveDescriptor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class MoveClass {

	public void performMoveClassRefactoring(IType origem, IPackageFragment destino) {	
		RefactoringContribution contribution2 = RefactoringCore.getRefactoringContribution(IJavaRefactorings.MOVE);
		MoveDescriptor descriptor2 = (MoveDescriptor) contribution2.createDescriptor();
		descriptor2.setMoveResources(new IFile[0],new IFolder[0],new ICompilationUnit[]{origem.getCompilationUnit()});
		descriptor2.setDestination(destino);
		descriptor2.setUpdateReferences(true);
		RefactoringStatus status2 = new RefactoringStatus();
		try {
			Refactoring refactoring2 = descriptor2.createRefactoring(status2);

			
			IProgressMonitor monitor2 = new NullProgressMonitor();
			refactoring2.checkInitialConditions(monitor2);
			refactoring2.checkFinalConditions(monitor2);
			Change change2 = refactoring2.createChange(monitor2);
			change2.perform(monitor2);

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
