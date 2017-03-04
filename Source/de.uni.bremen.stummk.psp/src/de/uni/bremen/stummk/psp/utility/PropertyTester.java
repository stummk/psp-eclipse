package de.uni.bremen.stummk.psp.utility;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Property Tester class, which tests if one project contains an psp file for popup mneu purpose
 * 
 * @author Konstantin
 *
 */
public class PropertyTester extends org.eclipse.core.expressions.PropertyTester {

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    IResource resource = null;

    if (receiver instanceof IAdaptable) {
      resource = (IResource) (((IAdaptable) receiver).getAdapter(IResource.class));
    } else if (receiver instanceof IResource) {
      resource = (IResource) receiver;
    }

    return CheckOperation.projectContainFile(resource);
  }

}
