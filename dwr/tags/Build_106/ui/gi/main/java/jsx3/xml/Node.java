package jsx3.xml;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A Node is a container of Records.
 * In use, Node has a parent Node and is part of a CdfDocument.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Node implements Iterable<Record>
{
    /**
     * You should not be directly creating Nodes
     */
    protected Node(String id)
    {
    	this.id = id;
    }

    /**
     * You should not be directly creating Nodes
     */
    protected Node()
    {
    }

    /**
     * A <code>NodeList</code> that contains all children of this node. If 
     * there are no children, this is a <code>NodeList</code> containing no 
     * nodes.
     */
    public Iterator<Record> iterator()
    {
        return children.iterator();
    }

    /**
     * Inserts the node <code>newChild</code> at the given <code>position</code>
     * @param position The position at which to insert the new Record
     * @param newRecord The node to insert.
     */
    public void insertRecord(int position, Record newRecord)
    {
        children.add(position, newRecord);

        if (newRecord.getOwnerDocument() != null)
        {
            newRecord.getParentNode().removeRecord(newRecord);
        }

        newRecord.joinDocument(this);
    }

    /**
     * Replaces the child Record <code>oldRecord</code> with <code>newRecord</code>
     * in the list of children, and returns the <code>oldRecord</code> Record.
     * If the <code>newRecord</code> is already in the tree, it is first removed.
     * @param newRecord The new Record to put in the child list.
     * @param oldRecord The Record being replaced in the list.
     */
    public void replaceRecord(Record newRecord, Record oldRecord)
    {
        int index = children.indexOf(oldRecord);
        if (index == -1)
        {
            appendRecord(newRecord);
        }
        else
        {
            removeRecord(oldRecord);
            insertRecord(index, oldRecord);
        }
    }

    /**
     * Removes the child Record indicated by <code>oldRecord</code> from the list 
     * of children, and returns it.
     * @param oldRecord The Record being removed.
     */
    public void removeRecord(Record oldRecord)
    {
        boolean removed = children.remove(oldRecord);
        if (removed)
        {
            oldRecord.leaveDocument();
        }
    }

    /**
     * Adds the Record <code>newRecord</code> to the end of the list of children 
     * of this Record. If the <code>newRecord</code> is already in the tree, it 
     * is first removed.
     * @param newRecord The Record to add.
     */
    public void appendRecord(Record newRecord)
    {
        if (newRecord.getOwnerDocument() != null)
        {
            newRecord.getParentNode().removeRecord(newRecord);
        }

        newRecord.joinDocument(this);
        children.add(newRecord);
    }

    /**
     * The parent of this node. The 
     * If a node has just been created and not yet added to the 
     * tree, or if it has been removed from the tree, this is 
     * <code>null</code>. 
     */
    public Node getParentNode()
    {
        return parentNode;
    }

    /**
     * The {@link CdfDocument} object associated with this node.
     * @return This Records parent Document
     */
    public CdfDocument getOwnerDocument()
    {
        return document;
    }

    /**
     * Remove all the nodes from this node
     */
    public void clear()
    {
        for (Record record : children)
        {
            removeRecord(record);
        }
    }

    /**
     * Internal setter for the parent node.
     * @param parent The new parent Node
     */
    protected void joinDocument(Node parent)
    {
        this.document = parent.getOwnerDocument();
        this.parentNode = parent;
    }

    /**
     * Internal way to disconnect a Node from a Document
     */
    protected void leaveDocument()
    {
        this.document = null;
        this.parentNode = null;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Node))
        {
            return false;
        }

        return super.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 37829 + id.hashCode();
    }

    /**
     * Local utility to create an indentation string
     * @param depth the number of indents that have been done
     * @return A string of depth <code>depth * 2</code>
     */
    protected static String indent(int depth)
    {
        StringBuilder reply = new StringBuilder(depth * 2);
        for (int i = 0; i < depth; i++)
        {
            reply.append("  ");
        }
        return reply.toString();
    }

    /**
     * The JSXID of this node
     */
    protected String id;

    /**
     * Our parent record
     */
    protected Node parentNode;

    /**
     * The CdfDocument that we are a part of
     */
    protected CdfDocument document;

    /**
     * The records that we contain
     */
    protected LinkedList<Record> children = new LinkedList<Record>();
}
