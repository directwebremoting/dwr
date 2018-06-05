package org.directwebremoting.datasync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.SortCriterion;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;

/**
 * This file is not supported in any way, and does not function correctly.
 * It might become part of DWR proper at some later date
 * @author Joe Walker [jwalker at sitepen dot com]
 */
class SqlStoreProvider<T> extends AbstractStoreProvider<T>
{
    /**
     *
     */
    private SqlStoreProvider(Class<T> type)
    {
        super(type);
    }

    /**
     * The primary key must be the first column
     */
    private static final String BASE_SQL = "SELECT col1, col2 FROM table";

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#view(org.directwebremoting.io.StoreRegion)
     */
    public MatchedItems view(StoreRegion region)
    {

        // Turn the query into a WHERE clause and an array of parameters
        List<String> params = new ArrayList<String>();
        StringBuilder where = new StringBuilder();
        for (Map.Entry<String, String> entry : region.getQuery().entrySet())
        {
            if (where.length() == 0)
            {
                where.append(" WHERE ");
            }
            else
            {
                where.append(" AND ");
            }

            String key = entry.getKey();
            where.append(key);
            where.append(" = :");
            where.append(key);

            params.add(entry.getValue());
        }

        // Create an ORDER BY clause
        StringBuilder orderBy = new StringBuilder();
        for (SortCriterion element : region.getSort())
        {
            if (where.length() == 0)
            {
                where.append(" ORDER BY ");
            }
            else
            {
                where.append(", ");
            }

            where.append(element.getAttribute());
            where.append(element.isAscending() ? "ASC" : "DESC");
        }

        // Create a LIMIT x OFFSET y clause
        int start = region.getStart();
        int count = region.getCount();
        String paging;
        if (start == 0)
        {
            if (count == -1)
            {
                // No paging
                paging = "";
            }
            else
            {
                // only as far as 'count'
                paging = " LIMIT " + count;
            }
        }
        else
        {
            if (count == -1)
            {
                throw new UnsupportedOperationException("Can't select a start point without selecting a count");
            }
            // only as far as 'count'
            paging = " LIMIT " + count + " OFFSET " + start;
        }

        String sql = BASE_SQL + where + orderBy + paging;

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Item> matchedValues = new ArrayList<Item>();

        try
        {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            int index = 1;
            for (String element : params)
            {
                stmt.setString(index++, element);
            }

            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // We're going to ignore the zeroth index to be like jdbc
            String[] columnNames = new String[columnCount + 1];
            for (int col = 1; col <= columnCount; col++)
            {
                columnNames[col] = metaData.getColumnName(col);
            }

            Map<String, String> data = new HashMap<String, String>();
            while (rs.next())
            {
                String itemId = rs.getString(1);
                for (int col = 2; col <= columnCount; col++)
                {
                    data.put(columnNames[col], rs.getString(col));
                }
                matchedValues.add(new Item(itemId, data));
            }
        }
        catch (SQLException ex)
        {
            log.warn("SQL Failure", ex);
        }
        finally
        {
            if (con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException ex2)
                {
                    log.warn("Error closing connection", ex2);
                }
            }

            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException ex2)
                {
                    log.warn("Error closing statement", ex2);
                }
            }

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException ex2)
                {
                    log.warn("Error closing resultset", ex2);
                }
            }
        }

        MatchedItems reply = new MatchedItems(matchedValues, -1);
        return reply;
    }

    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }


    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, java.lang.Object)
     */
    public void put(String itemId, T value)
    {
        throw new UnsupportedOperationException("SqlStoreProvider is read only");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.AbstractStoreProvider#getObject(java.lang.String)
     */
    @Override
    protected T getObject(String itemId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.io.StoreChangeListener)
     */
    public void unsubscribe(StoreChangeListener<T> listener)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#update(java.util.List)
     */
    public void update(List<ItemUpdate> changes)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewItem(java.lang.String, org.directwebremoting.io.StoreChangeListener)
     */
    public Item viewItem(String itemId, StoreChangeListener<T> listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.io.StoreRegion)
     */
    public MatchedItems viewRegion(StoreRegion region)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.io.StoreRegion, org.directwebremoting.io.StoreChangeListener)
     */
    public MatchedItems viewRegion(StoreRegion region, StoreChangeListener<T> listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SqlStoreProvider.class);
}
