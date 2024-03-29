package tp12.refactorings;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.ui.handlers.HandlerUtil;

import tp12.handlers.SampleHandler;

@SuppressWarnings("restriction")
public class MoveMethod {

	public static void performMoveMethod(IMethod method, String targetName) {

		try {
			MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(method,
					JavaPreferencesSettings.getCodeGenerationSettings(method.getJavaProject()));
			
			processor.checkInitialConditions(SingletonNullProgressMonitor.getNullProgressMonitor());

			IVariableBinding[] targets = processor.getPossibleTargets();
			IVariableBinding target = null;
			for (int i = 0; i < targets.length; i++) {
				if (targets[i].getType().getQualifiedName().compareTo(targetName) == 0) {
					target = targets[i];
					break;
				}
			}

			processor.setTarget(target);
			processor.setInlineDelegator(true);
			processor.setRemoveDelegator(true);
			processor.setDeprecateDelegates(false);

			Refactoring refactoring2 = new MoveRefactoring(processor);
			refactoring2.checkInitialConditions(SingletonNullProgressMonitor.getNullProgressMonitor());

			final CreateChangeOperation create2 = new CreateChangeOperation(
					new CheckConditionsOperation(refactoring2, CheckConditionsOperation.ALL_CONDITIONS),
					RefactoringStatus.FATAL);

			PerformChangeOperation perform2 = new PerformChangeOperation(create2);

			ResourcesPlugin.getWorkspace().run(perform2, SingletonNullProgressMonitor.getNullProgressMonitor());

		} catch (OperationCanceledException | CoreException e) {
			e.printStackTrace();
		} catch (AssertionFailedException e){
			MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event),"Informa��o sobre Viola��o", "N�o � poss�vel mover esse metodo automaticamente");
		}
		
	}
}
