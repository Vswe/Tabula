package us.ichun.mods.tabula.client.gui.window;

import us.ichun.mods.tabula.Tabula;
import us.ichun.mods.tabula.client.export.ExportList;
import us.ichun.mods.tabula.client.export.types.Exporter;
import us.ichun.mods.tabula.client.gui.GuiWorkspace;
import us.ichun.mods.tabula.client.gui.window.element.Element;
import us.ichun.mods.tabula.client.gui.window.element.ElementButton;
import us.ichun.mods.tabula.client.gui.window.element.ElementListTree;
import us.ichun.mods.tabula.client.gui.window.element.ElementToggle;
import us.ichun.module.tabula.client.model.ModelInfo;
import us.ichun.module.tabula.client.model.ModelList;

public class WindowExport extends Window
{
    public ElementListTree modelList;

    public WindowExport(GuiWorkspace parent, int x, int y, int w, int h, int minW, int minH)
    {
        super(parent, x, y, w, h, minW, minH, "window.importMC.title", true);

        elements.add(new ElementButton(this, width - 140, height - 22, 60, 16, 1, false, 1, 1, "element.button.ok"));
        elements.add(new ElementButton(this, width - 70, height - 22, 60, 16, 0, false, 1, 1, "element.button.cancel"));
        modelList = new ElementListTree(this, BORDER_SIZE + 1, BORDER_SIZE + 1 + 10, width - (BORDER_SIZE * 2 + 2), height - BORDER_SIZE - 22 - 16, 3, false, false);
        elements.add(modelList);

        for(Exporter model : ExportList.exportTypes)
        {
            modelList.createTree(null, model, 13, 0, false, false);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        super.draw(mouseX, mouseY);
    }

    @Override
    public void elementTriggered(Element element)
    {
        if(element.id == 0)
        {
            workspace.removeWindow(this, true);
        }
        if(element.id == 1)
        {
            if(!workspace.projectManager.projects.isEmpty())
            {
                boolean found = false;

                if(workspace.windowDragged == this)
                {
                    workspace.windowDragged = null;
                }
                for(int i = 0; i < modelList.trees.size(); i++)
                {
                    ElementListTree.Tree tree = modelList.trees.get(i);
                    if(tree.selected)
                    {
                        Exporter exporter = (Exporter)tree.attachedObject;
                        if(!exporter.override(workspace) && !exporter.export(workspace.projectManager.projects.get(workspace.projectManager.selectedProject)))
                        {
                            workspace.addWindowOnTop(new WindowPopup(workspace, 0, 0, 180, 80, 180, 80, "export.failed").putInMiddleOfScreen());
                        }
                        found = true;
                        break;
                    }
                }

                if(found)
                {
                    workspace.removeWindow(this, true);
                }
            }
        }
    }
}
