package us.ichun.mods.tabula.client.gui.window.element;

import ichun.client.render.RendererHelper;
import us.ichun.module.tabula.common.project.ProjectInfo;
import us.ichun.mods.tabula.client.gui.Theme;
import us.ichun.mods.tabula.client.gui.window.Window;
import us.ichun.mods.tabula.client.gui.window.WindowProjectSelection;

public class ElementProjectTab extends Element
{
    public ProjectInfo info;
    public boolean changed;

    public ElementProjectTab(Window window, int x, int y, int w, int h, int ID, ProjectInfo inf)
    {
        super(window, x, y, w, h, ID, true);
        info = inf;
        changed = false;
    }

    @Override
    public void draw(int mouseX, int mouseY, boolean hover)
    {
        WindowProjectSelection proj = (WindowProjectSelection)parent;
        if(id != proj.projects.size() - 1)
        {
            RendererHelper.drawColourOnScreen(Theme.tabSideInactive[0], Theme.tabSideInactive[1], Theme.tabSideInactive[2], 255, getPosX() + width - 1, getPosY() + 1, 1, height, 0);
        }
        if(proj.selectedProject == id)
        {
            RendererHelper.drawColourOnScreen(Theme.elementProjectTabActive[0], Theme.elementProjectTabActive[1], Theme.elementProjectTabActive[2], 255, getPosX(), getPosY(), width - 1, height, 0);
        }
        else if(hover)
        {
            RendererHelper.drawColourOnScreen(Theme.elementProjectTabHover[0], Theme.elementProjectTabHover[1], Theme.elementProjectTabHover[2], 255, getPosX(), getPosY(), width - 1, height, 0);
        }

        String titleToRender = info.modelName;
        while(titleToRender.length() > 1 && parent.workspace.getFontRenderer().getStringWidth(titleToRender) > width )
        {
            if(titleToRender.startsWith("... "))
            {
                break;
            }
            if(titleToRender.endsWith("... "))
            {
                titleToRender = titleToRender.substring(0, titleToRender.length() - 5) + "... ";
            }
            else
            {
                titleToRender = titleToRender.substring(0, titleToRender.length() - 1) + "... ";
            }
        }
        parent.workspace.getFontRenderer().drawString(titleToRender, parent.posX + posX + 4, parent.posY + posY + 3, Theme.getAsHex(changed? Theme.elementProjectTabFontChanges : Theme.elementProjectTabFont), false);
        if(!parent.workspace.remoteSession)
        {
            parent.workspace.getFontRenderer().drawString("X", parent.posX + posX + width - 8, parent.posY + posY + 3, Theme.getAsHex(Theme.elementProjectTabFont), false);
        }
    }

    @Override
    public void resized()
    {
        WindowProjectSelection tab = (WindowProjectSelection)parent;
        int space = tab.getWidth();
        int totalSpace = 0;
        for(ProjectInfo tab1 : tab.projects)
        {
            totalSpace += tab.workspace.getFontRenderer().getStringWidth(" " + tab1.modelName + (parent.workspace.remoteSession ? " " : " X "));
        }
        if(totalSpace > space)
        {
            posX = (id * (space / tab.projects.size()));
            posY = 0;
            width = space / tab.projects.size();
            height = 12;
        }
        else
        {
            posX = 0;
            for(int i = 0; i < id; i++)
            {
                posX += tab.workspace.getFontRenderer().getStringWidth(" " + tab.projects.get(i).modelName + (parent.workspace.remoteSession ? " " : " X "));
            }
            posY = 0;
            width = tab.workspace.getFontRenderer().getStringWidth(" " + info.modelName + (parent.workspace.remoteSession ? " " : " X "));
            height = 12;
        }
    }

    @Override
    public String tooltip()
    {
        //        String titleToRender = StatCollector.translateToLocal(mountedWindow.titleLocale);
        //        if(parent.workspace.getFontRenderer().getStringWidth(titleToRender) > width)
        //        {
        //            return mountedWindow.titleLocale;
        //        }
        String tooltip = info.modelName + " - " + info.authorName;
        if(info.saveFile != null)
        {
            tooltip = tooltip + " - " + info.saveFile.getName();
        }
        return tooltip; //return null for no tooltip. This is localized.
    }

    @Override
    public boolean onClick(int mouseX, int mouseY, int id)
    {
        if(id == 0)
        {
            if(mouseX + parent.posX > getPosX() + width - 9)
            {
                parent.workspace.closeProject(((WindowProjectSelection)parent).projects.get(this.id));
            }
            ((WindowProjectSelection)parent).changeProject(this.id);
        }
        return false;
    }
}
