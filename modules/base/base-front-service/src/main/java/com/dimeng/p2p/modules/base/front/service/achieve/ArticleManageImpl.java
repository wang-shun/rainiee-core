package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.EmptyPageResult;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.S50.enums.T5011_F05;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.entity.QuestionRecord;
import com.dimeng.p2p.modules.base.front.service.entity.QuestionTypeRecord;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_3_EXT;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_EXT;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;

public class ArticleManageImpl extends AbstractBaseService implements ArticleManage
{
    
    protected static final ArrayParser<T5011> ARRAY_PARSER = new ArrayParser<T5011>()
    {
        
        @Override
        public T5011[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<T5011> list = null;
            while (resultSet.next())
            {
                T5011 record = new T5011();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getInt(3);
                record.F04 = resultSet.getInt(4);
                record.F05 = T5011_F05.parse(resultSet.getString(5));
                record.F06 = resultSet.getString(6);
                record.F07 = resultSet.getString(7);
                record.F08 = resultSet.getString(8);
                record.F09 = resultSet.getString(9);
                record.F10 = resultSet.getInt(10);
                record.F11 = resultSet.getTimestamp(11);
                record.F12 = resultSet.getTimestamp(12);
                record.F13 = resultSet.getTimestamp(13);
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(record);
            }
            return list == null ? null : list.toArray(new T5011[list.size()]);
        }
    };
    
    protected static final ItemParser<T5011> ITEM_PARSER = new ItemParser<T5011>()
    {
        
        @Override
        public T5011 parse(ResultSet resultSet)
            throws SQLException
        {
            T5011 record = null;
            if (resultSet.next())
            {
                record = new T5011();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getInt(3);
                record.F04 = resultSet.getInt(4);
                record.F05 = T5011_F05.parse(resultSet.getString(5));
                record.F06 = resultSet.getString(6);
                record.F07 = resultSet.getString(7);
                record.F08 = resultSet.getString(8);
                record.F09 = resultSet.getString(9);
                record.F10 = resultSet.getInt(10);
                record.F11 = resultSet.getTimestamp(11);
                record.F12 = resultSet.getTimestamp(12);
                record.F13 = resultSet.getTimestamp(13);
            }
            return record;
        }
    };
    
    protected static final String SELECT_ALL_SQL =
        "SELECT T5011.F01, T5011.F02, T5011.F03, T5011.F04, T5011.F05, T5011.F06, T5011.F07, T5011.F08, T5011.F09, T5011.F10, T5011.F11, T5011.F12, T5011.F13 FROM S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 ";
    
    protected static final String SELECT_ALL_SQL_EXT =
        "SELECT T5011.F01, T5011.F02, T5011.F03, T5011.F04, T5011.F05, T5011.F06, T5011.F07, T5011.F08, T5011.F09, T5011.F10, T5011.F11, T5011.F12, T5011.F13, T5011_1.F02 FROM S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 INNER JOIN S50.T5011_1 ON T5011.F01 = T5011_1.F01 ";
    
    public ArticleManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T5011 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5011.F01  =? AND T5011.F05  =?");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), id, T5011_F05.YFB.name());
        }
    }
    
    @Override
    public PagingResult<T5011> search(T5011_F02 articleType, Paging paging)
        throws Throwable
    {
        if (articleType == null)
        {
            return new EmptyPageResult<T5011>();
        }
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE T5011.F05 = ?  AND T5010.F02 = ? AND T5010.F04=?");
        sql.append("ORDER BY T5011.F04 ASC, T5011.F12 DESC, T5011.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                ARRAY_PARSER,
                paging,
                sql.toString(),
                T5011_F05.YFB.name(),
                articleType.name(),
                T5010_F04.QY.name());
        }
    }
    
    @Override
    public String getContent(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection();
            PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5011_1.F02 FROM S50.T5011_1 INNER JOIN S50.T5011 ON T5011.F01 = T5011_1.F01 WHERE T5011_1.F01 = ? AND T5011.F05 = ?"))
        {
            pstmt.setInt(1, id);
            pstmt.setString(2, T5011_F05.YFB.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public void view(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S50.T5011 SET F03 = F03 + 1 WHERE F01 = ? AND F05 = ?",
                id,
                T5011_F05.YFB.name());
        }
    }
    
    @Override
    public T5011 get(T5011_F02 articleType)
        throws Throwable
    {
        if (articleType == null)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5010.F02 = ? AND T5011.F05  =? ORDER BY T5011.F04 DESC LIMIT 1");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), articleType.name(), T5011_F05.YFB.name());
        }
    }
    
    @Override
    public String getContent(T5011_F02 articleType)
        throws Throwable
    {
        if (articleType == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            int id;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 WHERE T5010.F02 = ? AND T5011.F05 = ? ORDER BY T5011.F04 DESC LIMIT 1"))
            {
                pstmt.setString(1, articleType.name());
                pstmt.setString(2, T5011_F05.YFB.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        id = resultSet.getInt(1);
                    }
                    else
                    {
                        return null;
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5011_1.F02 FROM S50.T5011_1 INNER JOIN S50.T5011 ON T5011.F01 = T5011_1.F01 WHERE T5011_1.F01 = ? "))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T5011 get(int id, T5011_F02 articleType)
        throws Throwable
    {
        if (id <= 0 || articleType == null)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5011.F01  =? AND T5010.F02 = ? AND T5011.F05  =?");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), id, articleType.name(), T5011_F05.YFB);
        }
    }
    
    @Override
    public QuestionTypeRecord[] getQuestionTypes(T5011_F02 articleType)
        throws Throwable
    {
        ArrayList<QuestionTypeRecord> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F04 FROM S50.T5021 WHERE F09 = ? AND F03 = ?"))
            {
                pstmt.setString(1, articleType.name());
                pstmt.setString(2, ArticlePublishStatus.YFB.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        QuestionTypeRecord record = new QuestionTypeRecord();
                        record.id = resultSet.getInt(1);
                        record.questionType = resultSet.getString(2);
                        record.imageCode = resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new QuestionTypeRecord[list.size()]));
    }
    
    @Override
    public QuestionRecord[] getQuestions(T5011_F02 articleType, int questionID)
        throws Throwable
    {
        ArrayList<QuestionRecord> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F09,F10 FROM S50.T5021_1 WHERE F11 = ? AND F02 = ? AND F03 = ?"))
            {
                pstmt.setString(1, articleType.name());
                pstmt.setInt(2, questionID);
                pstmt.setString(3, ArticlePublishStatus.YFB.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        QuestionRecord record = new QuestionRecord();
                        record.id = resultSet.getInt(1);
                        record.question = resultSet.getString(2);
                        record.questionAnswer = resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new QuestionRecord[list.size()]));
    }
    
    @Override
    public T5010[] getArticleCategoryAll(int parentId, T5010_F04 status)
        throws Throwable
    {
        ArrayList<T5010> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S50.T5010 WHERE  F04 = ? AND F05 = ?"))
            {
                pstmt.setString(1, status.name());
                pstmt.setInt(2, parentId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T5010 record = new T5010();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = T5010_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getInt(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T5010[list.size()]));
    }
    
    @Override
    public String getCategoryNameByCode(String code)
        throws Throwable
    {
        String name = "";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S50.T5010 WHERE  F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, code);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        name = resultSet.getString(1);
                    }
                }
            }
            return name;
        }
    }
    
    @Override
    public T5010_F04 getCategoryStatusByCode(String code)
        throws Throwable
    {
        T5010_F04 status = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S50.T5010 WHERE  F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, code);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        status = T5010_F04.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return status;
    }
    
    @Override
    public T5011 get(String articleType)
        throws Throwable
    {
        if (articleType == null)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5010.F02 = ? AND T5011.F05  =? ORDER BY T5011.F04 DESC LIMIT 1");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), articleType, T5011_F05.YFB.name());
        }
    }
    
    @Override
    public PagingResult<T5011_EXT> search_EXT(String articleType, Paging paging, final FileStore fileStore)
        throws Throwable
    {
        if (articleType == null)
        {
            return new EmptyPageResult<T5011_EXT>();
        }
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL_EXT);
        sql.append(" WHERE T5011.F05 = ?  AND T5010.F02 = ? AND T5010.F04=?");
        sql.append("ORDER BY T5011.F04 ASC, T5011.F12 DESC, T5011.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T5011_EXT>()
            {
                
                @Override
                public T5011_EXT[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T5011_EXT> list = null;
                    while (resultSet.next())
                    {
                        T5011_EXT record = new T5011_EXT();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T5011_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getString(9);
                        if (!StringHelper.isEmpty(record.F09))
                        {
                            record.F09 = fileStore.getURL(record.F09);
                        }
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getTimestamp(11);
                        record.F12 = resultSet.getTimestamp(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.content = resultSet.getString(14);
                        record.lastUPdateTime = Formater.formatDate(record.F13);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null ? null : list.toArray(new T5011_EXT[list.size()]);
                }
            }, paging, sql.toString(), T5011_F05.YFB.name(), articleType, T5010_F04.QY.name());
        }
    }
    
    @Override
    public QuestionTypeRecord[] getQuestionTypes(String articleType, FileStore fileStore)
        throws Throwable
    {
        ArrayList<QuestionTypeRecord> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F04 FROM S50.T5021 WHERE F09 = ? AND F03 = ?"))
            {
                pstmt.setString(1, articleType);
                pstmt.setString(2, ArticlePublishStatus.YFB.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        QuestionTypeRecord record = new QuestionTypeRecord();
                        record.id = resultSet.getInt(1);
                        record.questionType = resultSet.getString(2);
                        record.imageCode = fileStore.getURL(resultSet.getString(3));
                        record.questionRecord = getQuestions(articleType, record.id, connection, fileStore);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new QuestionTypeRecord[list.size()]));
    }
    
    private QuestionRecord[] getQuestions(String articleType, int questionID, Connection connection, FileStore fileStore)
        throws Throwable
    {
        ArrayList<QuestionRecord> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F09,F10 FROM S50.T5021_1 WHERE F11 = ? AND F02 = ? AND F03 = ?"))
        {
            pstmt.setString(1, articleType);
            pstmt.setInt(2, questionID);
            pstmt.setString(3, ArticlePublishStatus.YFB.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    QuestionRecord record = new QuestionRecord();
                    record.id = resultSet.getInt(1);
                    record.question = resultSet.getString(2);
                    record.questionAnswer = StringHelper.format(resultSet.getString(3), fileStore);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new QuestionRecord[list.size()]));
    }
    
    @Override
    public String[] getYear()
        throws Throwable
    {
        List<String> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT DISTINCT T5011_3.F06 FROM S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 INNER JOIN S50.T5011_3 ON T5011.F01 = T5011_3.F02 WHERE T5011.F05 = ?  AND T5010.F02 = ? AND T5010.F04=? ORDER BY T5011_3.F06 DESC, T5011.F12 DESC, T5011.F13 DESC LIMIT 5 "))
            {
                pstmt.setString(1, T5011_F05.YFB.name());
                pstmt.setString(2, "YYBG");
                pstmt.setString(3, T5010_F04.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(resultSet.getString(1));
                        
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new String[list.size()]));
    }
    
    @Override
    public PagingResult<T5011_3_EXT> searchInformation(String articleType, String year, Paging paging,
        final FileStore fileStore)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T5011.F01 AS F01, T5011.F02 AS F02, T5011.F03 AS F03, T5011.F04 AS F04, T5011.F05 AS F05, T5011.F06 AS F06, T5011.F07 AS F07, T5011.F08 AS F08, T5011.F09 AS F09, T5011.F10 AS F10, ");
        sql.append(" T5011.F11 AS F11, T5011.F12 AS F12, T5011.F13 AS F13,T5011_3.F01 AS F14,T5011_3.F02 AS F15,T5011_3.F03 AS F16,T5011_3.F04 AS F17,T5011_3.F05 AS F18,T5011_3.F06 AS F19 FROM ");
        sql.append("S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 INNER JOIN S50.T5011_3 ON T5011.F01 = T5011_3.F02 ");
        sql.append(" WHERE T5011.F05 = ?  AND T5010.F04=? ");
        List<Object> parameters = new ArrayList<>();
        parameters.add(T5011_F05.YFB.name());
        parameters.add(T5010_F04.QY.name());
        if (!StringHelper.isEmpty(articleType))
        {
            sql.append(" AND T5010.F02 = ? ");
            parameters.add(articleType);
        }
        if (!StringHelper.isEmpty(year))
        {
            sql.append(" AND T5011_3.F06=?");
            parameters.add(year);
        }
        sql.append("ORDER BY T5011.F04 ASC, T5011.F12 DESC, T5011.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T5011_3_EXT>()
            {
                
                @Override
                public T5011_3_EXT[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T5011_3_EXT> list = null;
                    while (resultSet.next())
                    {
                        T5011_3_EXT record = new T5011_3_EXT();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T5011_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getString(9);
                        if (!StringHelper.isEmpty(record.F09))
                        {
                            record.F09 = fileStore.getURL(record.F09);
                        }
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getTimestamp(11);
                        record.F12 = resultSet.getTimestamp(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.id = resultSet.getInt(14);
                        record.articleId = resultSet.getInt(15);
                        record.path = fileStore.getURL(resultSet.getString(16));
                        record.size = resultSet.getString(17);
                        record.name = resultSet.getString(18);
                        record.year = resultSet.getString(19);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null ? null : list.toArray(new T5011_3_EXT[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public T5011_3 getFileInfo(String id)
        throws Throwable
    {
        T5011_3 info = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S50.T5011_3 WHERE F01 = ?"))
            {
                pstmt.setString(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info = new T5011_3();
                        info.F01 = resultSet.getInt(1);
                        info.F02 = resultSet.getInt(2);
                        info.F03 = resultSet.getString(3);
                        info.F04 = resultSet.getString(4);
                    }
                    return info;
                }
            }
        }
    }
}
