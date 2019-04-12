/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.eclipse.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;

import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.eclipse.plugin.PMDPlugin;

public class PMDMarkerImageProvider implements IAnnotationImageProvider {

    @Override
    public ImageDescriptor getImageDescriptor(String imageDescriptorId) {
        return null;
    }

    @Override
    public String getImageDescriptorId(Annotation annotation) {
        return null;
    }

    @Override
    public Image getManagedImage(Annotation annotation) {
        PMDPlugin plugin = PMDPlugin.getDefault();
        String type = annotation.getType();
        if ("net.sourceforge.pmd.eclipse.plugin.annotation.prio1".equals(type)) {
            return plugin.getImage(type, "icons/markerP1.png");
        } else if ("net.sourceforge.pmd.eclipse.plugin.annotation.prio2".equals(type)) {
            return plugin.getImage(type, "icons/markerP2.png");
        } else if ("net.sourceforge.pmd.eclipse.plugin.annotation.prio3".equals(type)) {
            return plugin.getImage(type, "icons/markerP3.png");
        } else if ("net.sourceforge.pmd.eclipse.plugin.annotation.prio4".equals(type)) {
            return plugin.getImage(type, "icons/markerP4.png");
        } else if ("net.sourceforge.pmd.eclipse.plugin.annotation.prio5".equals(type)) {
            return plugin.getImage(type, "icons/markerP5.png");
        }
        return null;
    }

    public static void removeCachedImages() {
        ImageRegistry imageRegistry = PMDPlugin.getDefault().getImageRegistry();
        for (RulePriority priority : RulePriority.values()) {
            imageRegistry.remove("net.sourceforge.pmd.eclipse.plugin.annotation.prio" + priority.getPriority());
        }
    }
}
