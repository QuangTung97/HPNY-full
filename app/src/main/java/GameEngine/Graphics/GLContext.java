package GameEngine.Graphics;

import java.util.ArrayList;
import java.util.List;

import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Abstract.VertexBuffer;

import GameEngine.Basic.ListHead;
import GameEngine.Graphics.Implement.ArrayListInt;
import GameEngine.Graphics.Implement.ArrayListObject;

/**
 * Created by Quang Tung on 12/25/2015.
 */
public class GLContext {

    //Table 1 used for Context
    List<ListHead<VertexBuffer>> VBListTable = new ArrayList<>(Shader.MAX_PRIORITY);
    List<Shader> shaderTable = new ArrayList<>(Shader.MAX_PRIORITY);

    //Table 2 used for Commands
    private final ArrayListInt cmdTypeTable = new ArrayListInt(100);
    private ArrayListObject param1Table = new ArrayListObject(100);
    private ArrayListObject param2Table = new ArrayListObject(100);

    private static final int ADD_VB_CMD = 1;
    private static final int REPLACE_VB_CMD = 2;
    private static final int REMOVE_VB_CMD = 3;

    public GLContext() {
        //Initialize table 1
        for (int i = 0; i < Shader.MAX_PRIORITY; i++) {
            VBListTable.add(new ListHead<VertexBuffer>(null));
            shaderTable.add(null);
        }
    }

    //Run in Render Thread
    public void doCommands() {
        synchronized (cmdTypeTable) {
            int size = cmdTypeTable.size();

            for (int i = 0; i < size; i++) {

                switch (cmdTypeTable.get(i)) {

                    case ADD_VB_CMD:
                        addVB(param1Table.get(i), param2Table.get(i));
                        break;

                    case REPLACE_VB_CMD:
                        replaceVB(param1Table.get(i), param2Table.get(i));
                        break;

                    case REMOVE_VB_CMD:
                        removeVB(param1Table.get(i));
                        break;

                    default:
                        break;
                }
            }

            //Clear table2 data
            cmdTypeTable.clear();
            param1Table.clear();
            param2Table.clear();
        }
    }


    //Run in any thread
    public void addVB(Shader shader, VertexBuffer buffer) {
        synchronized (cmdTypeTable) {
            cmdTypeTable.add(ADD_VB_CMD);
            param1Table.add(shader);
            param2Table.add(buffer);
        }
    }

    public void replaceVB(VertexBuffer oldVB, VertexBuffer newVB) {
        synchronized (cmdTypeTable) {
            cmdTypeTable.add(REPLACE_VB_CMD);
            param1Table.add(oldVB);
            param1Table.add(newVB);
        }
    }

    public void removeVB(VertexBuffer vb) {
        synchronized (cmdTypeTable) {
            cmdTypeTable.add(REMOVE_VB_CMD);
            param1Table.add(vb);
            param2Table.add(null);
        }
    }

    //Run in Render Thread of functions above
    private void addVB(Object shader, Object VB) {
        Shader shader1 = (Shader)shader;
        VertexBuffer vb = (VertexBuffer)VB;

        if (shaderTable.get(shader1.priority) == null) {
            shaderTable.set(shader1.priority, shader1);
            shader1.create();
        }

        VBListTable.get(shader1.priority).addLast(vb.list);
    }

    private void replaceVB(Object oldVB, Object newVB) {
        VertexBuffer oldBuffer = (VertexBuffer)oldVB;
        VertexBuffer newBuffer = (VertexBuffer)newVB;

        oldBuffer.list.replace(newBuffer.list);
    }

    private void removeVB(Object vb) {
        VertexBuffer buffer = (VertexBuffer)vb;
        buffer.list.remove();
    }
}
