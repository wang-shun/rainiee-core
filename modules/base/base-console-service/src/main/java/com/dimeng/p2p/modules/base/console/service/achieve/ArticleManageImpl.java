package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.Article;
import com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord;
import com.dimeng.p2p.modules.base.console.service.entity.OperateReport;
import com.dimeng.p2p.modules.base.console.service.entity.Question;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionRecord;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord;
import com.dimeng.p2p.modules.base.console.service.query.ArticleQuery;
import com.dimeng.p2p.modules.base.console.service.query.OperateReportQuery;
import com.dimeng.p2p.modules.base.console.service.query.QuestionQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

public class ArticleManageImpl extends AbstractInformationService implements ArticleManage
{
    
    public static class ArticleManageFactory implements ServiceFactory<ArticleManage>
    {
        @Override
        public ArticleManage newInstance(ServiceResource serviceResource)
        {
            return new ArticleManageImpl(serviceResource);
        }
    }
    
    protected static final ArrayParser<ArticleRecord> ARRAY_PARSER = new ArrayParser<ArticleRecord>()
    {
        
        @Override
        public ArticleRecord[] parse(ResultSet rs)
            throws SQLException
        {
            ArrayList<ArticleRecord> list = null;
            while (rs.next())
            {
                ArticleRecord news = new ArticleRecord();
                news.id = rs.getInt(1);
                news.categoryId = rs.getInt(2);
                news.viewTimes = rs.getInt(3);
                news.sortIndex = rs.getInt(4);
                news.publishStatus = ArticlePublishStatus.valueOf(rs.getString(5));
                news.title = rs.getString(6);
                news.source = rs.getString(7);
                news.summary = rs.getString(8);
                news.imageCode = rs.getString(9);
                news.publisherId = rs.getInt(10);
                news.createtime = rs.getTimestamp(11);
                news.publishTime = rs.getTimestamp(12);
                news.publisherName = rs.getString(13);
                news.categoryCode = rs.getString(14);
                news.categoryName = rs.getString(15);
                news.categoryStatus = T5010_F04.parse(rs.getString(16));
                news.updateTime = rs.getTimestamp(17);
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(news);
            }
            return list == null ? null : list.toArray(new ArticleRecord[list.size()]);
        }
    };
    
    protected static final ItemParser<ArticleRecord> ITEM_PARSER = new ItemParser<ArticleRecord>()
    {
        
        @Override
        public ArticleRecord parse(ResultSet rs)
            throws SQLException
        {
            ArticleRecord news = null;
            if (rs.next())
            {
                news = new ArticleRecord();
                news.id = rs.getInt(1);
                news.categoryId = rs.getInt(2);
                news.viewTimes = rs.getInt(3);
                news.sortIndex = rs.getInt(4);
                news.publishStatus = ArticlePublishStatus.valueOf(rs.getString(5));
                news.title = rs.getString(6);
                news.source = rs.getString(7);
                news.summary = rs.getString(8);
                news.imageCode = rs.getString(9);
                news.publisherId = rs.getInt(10);
                news.createtime = rs.getTimestamp(11);
                news.publishTime = rs.getTimestamp(12);
                news.publisherName = rs.getString(13);
                news.categoryCode = rs.getString(14);
                news.categoryName = rs.getString(15);
                news.categoryStatus = T5010_F04.parse(rs.getString(16));
            }
            return news;
        }
    };
    
    protected static final String SELECT_ALL_SQL =
        "SELECT T5011.F01,T5011.F02,T5011.F03,T5011.F04,T5011.F05,T5011.F06,T5011.F07,T5011.F08,T5011.F09,T5011.F10,T5011.F11,T5011.F12,T7110.F04 AS F13,T5010.F02 AS F14,T5010.F03 AS F15,T5010.F04 AS F16,T5011.F13 AS F17 FROM S50.T5011 INNER JOIN S71.T7110 ON T5011.F10 = T7110.F01 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01";
    
    public ArticleManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<ArticleRecord> search(ArticleQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            T5011_F02 articleType = query.getType();
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (articleType != null)
            {
                sql.append(" AND T5010.F02 = ?");
                parameters.add(articleType.name());
            }
            ArticlePublishStatus publishStatus = query.getPublishStatus();
            if (publishStatus != null)
            {
                sql.append(" AND T5011.F05 = ?");
                parameters.add(publishStatus.name());
            }
            String string = query.getTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5011.F06 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F11) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F11) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getPublishTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F12) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getPublishTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F12) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getLastUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F13) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getLastUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F13) <= ?");
                parameters.add(timestamp);
            }
            string = query.getSource();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5011.F07 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = query.getSummary();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5011.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = query.getPublisherName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
        }
        sql.append(" ORDER BY T5011.F04,T5011.F12 DESC,T5011.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public ArticleRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5011.F01  =?");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), id);
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
            PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM T5011_1 WHERE F01 = ?"))
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
        return null;
    }
    
    public void setPublishStatus(ArticlePublishStatus publishStatus, Connection connection, int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0 || publishStatus == null)
        {
            return;
        }
        try (PreparedStatement pstmt =
            getConnection().prepareStatement("UPDATE T5011 SET F05  = ? WHERE F01 = ? AND F05 = ?"))
        {
            for (int id : ids)
            {
                if (id <= 0)
                {
                    continue;
                }
                pstmt.setString(1, publishStatus.name());
                pstmt.setInt(2, id);
                pstmt.setString(3, publishStatus == ArticlePublishStatus.YFB ? ArticlePublishStatus.WFB.name()
                    : ArticlePublishStatus.YFB.name());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    @Override
    public void setTop(boolean top, int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        int sortIndex = top ? Integer.MAX_VALUE : 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE T5011 SET F04  = ?, F12  = ? WHERE F01 = ? "))
            {
                for (int id : ids)
                {
                    if (id <= 0)
                    {
                        continue;
                    }
                    pstmt.setInt(1, sortIndex);
                    pstmt.setTimestamp(2, now);
                    pstmt.setInt(3, id);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        }
    }
    
    @Override
    public void delete(int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 删除附件列表
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5011_2 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                // 删除文章内容
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5011_1 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                // 删除文章记录
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5011 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void update(int id, Article article)
        throws Throwable
    {
        if (id <= 0 || article == null)
        {
            return;
        }
        String title;
        String source;
        String imageCode = null;
        String content = null;
        ArticlePublishStatus publishStatus = article.getPublishStatus();
        {
            // 校验
            title = article.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("标题不能为空.");
            }
            if (title.length() > 30)
            {
                throw new ParameterException("标题不能超过30个字.");
            }
            source = article.getSource();
            if (!StringHelper.isEmpty(source) && source.length() > 50)
            {
                throw new ParameterException("文章来源不能超过50个字.");
            }
            content = article.getContent();
            if (StringHelper.isEmpty(content) && !T5011_F02.BYJDFWB.getChineseName().equals(title))
            {
                throw new ParameterException("文章内容不能为空");
            }
        }
        
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = article.getImage();
        if (uploadFile != null)
        {// 保存封面图片
            imageCode = fileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), uploadFile)[0];
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM T5011 WHERE F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new LoginException("文章不存在.");
                        }
                    }
                }
                // 修改记录信息
                if (StringHelper.isEmpty(imageCode))
                {
                    execute(connection,
                        "UPDATE T5011 SET F04 = ?, F06 = ?, F07 = ?, F08  =? , F13 = CURRENT_TIMESTAMP() WHERE F01 = ?",
                        article.getSortIndex(),
                        title,
                        source,
                        article.getSummary(),
                        id);
                }
                else
                {
                    execute(connection,
                        "UPDATE T5011 SET F04 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ? ,F13 = CURRENT_TIMESTAMP() WHERE F01 = ?",
                        article.getSortIndex(),
                        title,
                        source,
                        article.getSummary(),
                        imageCode,
                        id);
                }
                // 修改内容
                execute(connection, "UPDATE T5011_1 SET F02 = ? WHERE F01 = ?", content, id);
                
                if (publishStatus != null)
                {
                    setPublishStatus(publishStatus, connection, id);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public int add(T5011_F02 articleType, Article article)
        throws Throwable
    {
        if (articleType == null)
        {
            throw new ParameterException("没有指定文章类型");
        }
        String title;
        String source;
        String imageCode = null;
        String content = null;
        ArticlePublishStatus publishStatus = null;
        {
            // 校验
            title = article.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("标题不能为空.");
            }
            if (title.length() > 30)
            {
                throw new ParameterException("标题不能超过30个字.");
            }
            source = article.getSource();
            if (!StringHelper.isEmpty(source) && source.length() > 50)
            {
                throw new ParameterException("文章来源不能超过50个字.");
            }
            
            publishStatus = article.getPublishStatus();
            if (publishStatus == null)
            {
                publishStatus = ArticlePublishStatus.WFB;
            }
            if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
            {
                content = article.getContent();
                if (StringHelper.isEmpty(content))
                {
                    throw new ParameterException("文章内容不能为空");
                }
            }
            
        }
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = article.getImage();
        if (uploadFile != null)
        {// 保存封面图片
            imageCode = fileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), uploadFile)[0];
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                T5010 t5010 = getArticleTypeByCode(articleType.name());
                int id =
                    insert(connection,
                        "INSERT INTO T5011 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ?,F12 = ?",
                        t5010.F01,
                        0,
                        article.getSortIndex(),
                        publishStatus.name(),
                        title,
                        source,
                        article.getSummary(),
                        imageCode,
                        serviceResource.getSession().getAccountId(),
                        now,
                        article.publishTime());
                if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
                {
                    execute(connection, "INSERT INTO T5011_1 SET F01 = ?, F02 = ?", id, content);
                }
                serviceResource.commit(connection);
                return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public String[] getAttachments(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM T5011_2 WHERE F01 = ? "))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    ArrayList<String> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(resultSet.getString(1));
                    }
                    return list == null ? null : list.toArray(new String[list.size()]);
                }
            }
        }
    }
    
    @Override
    public void addAttachments(int id, UploadFile... attachments)
        throws Throwable
    {
        if (id <= 0 || attachments == null || attachments.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S50.T5011 WHERE F01 = ?"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new LoginException("文章不存在.");
                        }
                    }
                }
                FileStore fileStore = serviceResource.getResource(FileStore.class);
                String[] attachmentCodes = fileStore.upload(FileType.ARTICLE_ATTACHMENT.ordinal(), attachments);
                // 保存附件
                if (attachmentCodes != null)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S50.T5011_2 SET F01 = ? AND F02 = ?"))
                    {
                        for (String attachmentCode : attachmentCodes)
                        {
                            pstmt.setInt(1, id);
                            pstmt.setString(2, attachmentCode);
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void deleteAttachments(int id, String... attachmentCodes)
        throws Throwable
    {
        if (id <= 0 || attachmentCodes == null || attachmentCodes.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S50.T5011 WHERE F01 = ? "))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new LoginException("文章不存在.");
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("DELETE FROM S50.T5011_2 WHERE F01 = ? AND F02 = ?"))
                {
                    for (String attachmentCode : attachmentCodes)
                    {
                        if (StringHelper.isEmpty(attachmentCode))
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.setString(2, attachmentCode);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public ArticleRecord get(T5011_F02 articleType)
        throws Throwable
    {
        if (articleType == null)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5010.F02 = ? LIMIT 1");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), articleType.name());
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
                connection.prepareStatement("SELECT T5011.F01 FROM S50.T5011 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01 WHERE T5010.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, articleType.name());
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
    public ArticleRecord get(int id, T5011_F02 articleType)
        throws Throwable
    {
        if (id <= 0 || articleType == null)
        {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT_ALL_SQL);
        sql.append(" WHERE T5011.F01  =? AND T5010.F02 = ?");
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), id, articleType.name());
        }
    }
    
    @Override
    public PagingResult<QuestionRecord> searchCzytxWt(QuestionQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T5021_1.F01 AS F01, T5021_1.F02 AS F02, T5021_1.F03 AS F03, T5021_1.F04 AS F04, T5021_1.F05 AS F05, T5021_1.F06 AS F06, T5021_1.F07 AS F07, T5021_1.F08 AS F08, T5021_1.F09 AS F09, T7110.F04 AS F10, T5021.F02 AS F11 FROM S50.T5021_1 INNER JOIN S71.T7110 ON T5021_1.F05 = T7110.F01 INNER JOIN S50.T5021 ON T5021_1.F02 = T5021.F01");
        sql.append(" WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            T5011_F02 articleType = query.getArticleType();
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (articleType != null)
            {
                sql.append(" AND T5021_1.F11 = ?");
                parameters.add(articleType.name());
            }
            ArticlePublishStatus publishStatus = query.getPublishStatus();
            if (publishStatus != null)
            {
                sql.append(" AND T5021_1.F03 = ?");
                parameters.add(publishStatus.name());
            }
            int qtid = query.getQuestionTypeID();
            if (qtid > 0)
            {
                sql.append(" AND T5021_1.F02 = ?");
                parameters.add(qtid);
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5021_1.F08) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5021_1.F08) <= ?");
                parameters.add(timestamp);
            }
            
            String publisher = query.getPublisher();
            if (!StringHelper.isEmpty(publisher))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(publisher));
            }
            
            String question = query.getQuestion();
            if (!StringHelper.isEmpty(question))
            {
                sql.append(" AND T5021_1.F09 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(question));
            }
        }
        sql.append(" ORDER BY T5021_1.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<QuestionRecord>()
            {
                
                @Override
                public QuestionRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<QuestionRecord> list = null;
                    while (rs.next())
                    {
                        QuestionRecord record = new QuestionRecord();
                        record.id = rs.getInt(1);
                        record.questionType = rs.getString(11);
                        record.publishStatus = ArticlePublishStatus.valueOf(rs.getString(3));
                        record.imageCode = rs.getString(4);
                        record.publisherId = rs.getInt(5);
                        record.publisherName = rs.getString(10);
                        record.createtime = rs.getTimestamp(6);
                        record.publishTime = rs.getTimestamp(7);
                        record.lastModifyTime = rs.getTimestamp(8);
                        record.question = rs.getString(9);
                        record.articleType = T5011_F02.parse(rs.getString(11));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new QuestionRecord[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<QuestionTypeRecord> searchCzytxWtlx(QuestionQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T5021.F01 AS F01,T5021.F02 AS F02,T5021.F03 AS F03,T5021.F04 AS F04,T5021.F05 AS F05,T5021.F06 AS F06,T5021.F07 AS F07,T5021.F08 AS F08,T5021.F09 AS F09,T7110.F04 AS F10 FROM S50.T5021 INNER JOIN S71.T7110 ON T5021.F05 = T7110.F01 ");
        sql.append(" WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            T5011_F02 articleType = query.getArticleType();
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (articleType != null)
            {
                sql.append(" AND T5021.F09 = ?");
                parameters.add(articleType.name());
            }
            ArticlePublishStatus publishStatus = query.getPublishStatus();
            if (publishStatus != null)
            {
                sql.append(" AND T5021.F03 = ?");
                parameters.add(publishStatus.name());
            }
            String string = query.getQuestionType();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5021.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5021.F08) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5021.F08) <= ?");
                parameters.add(timestamp);
            }
            
            String publisher = query.getPublisher();
            if (!StringHelper.isEmpty(publisher))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(publisher));
            }
        }
        sql.append(" ORDER BY T5021.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<QuestionTypeRecord>()
            {
                
                @Override
                public QuestionTypeRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<QuestionTypeRecord> list = null;
                    while (rs.next())
                    {
                        QuestionTypeRecord record = new QuestionTypeRecord();
                        record.id = rs.getInt(1);
                        record.questionType = rs.getString(2);
                        record.publishStatus = ArticlePublishStatus.valueOf(rs.getString(3));
                        record.imageCode = rs.getString(4);
                        record.publisherId = rs.getInt(5);
                        record.publisherName = rs.getString(10);
                        record.createtime = rs.getTimestamp(6);
                        record.publishTime = rs.getTimestamp(7);
                        record.articleType = T5011_F02.parse(rs.getString(9));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new QuestionTypeRecord[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public int addQuestionType(T5011_F02 articleType, Question questionType)
        throws Throwable
    {
        if (articleType == null)
        {
            throw new ParameterException("没有指定问题类型");
        }
        if (isExistQuestionType(articleType, questionType.getQuestionType()))
        {
            throw new ParameterException("问题类型已经存在");
        }
        String imageCode = null;
        
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = questionType.getImage();
        if (uploadFile != null)
        {// 保存封面图片
            imageCode = fileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), uploadFile)[0];
        }
        try (Connection connection = getConnection())
        {
            try
            {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                int id =
                    insert(connection,
                        "INSERT INTO T5021 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?",
                        questionType.getQuestionType(),
                        questionType.getPublishStatus().name(),
                        imageCode,
                        serviceResource.getSession().getAccountId(),
                        now,
                        questionType.publishTime(),
                        now,
                        articleType.name());
                return id;
            }
            catch (Exception e)
            {
                logger.error("ArticleManageImpl.addQuestionType() error", e);
                throw e;
            }
        }
    }
    
    @Override
    public int addQuestion(T5011_F02 articleType, Question question)
        throws Throwable
    {
        if (articleType == null)
        {
            throw new ParameterException("没有指定问题");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                int id =
                    insert(connection,
                        "INSERT INTO T5021_1 SET F02 = ?,F03 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ?",
                        question.getQuestionTypeID(),
                        question.getPublishStatus().name(),
                        serviceResource.getSession().getAccountId(),
                        now,
                        question.publishTime(),
                        now,
                        question.getQuestion(),
                        question.getQuestionAnswer(),
                        articleType.name());
                return id;
            }
            catch (Exception e)
            {
                logger.error("ArticleManageImpl.addQuestion() error", e);
                throw e;
            }
        }
    }
    
    @Override
    public void updateQuestion(T5011_F02 articleType, Question question, int id)
        throws Throwable
    {
        if (id <= 0 || question == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM T5021_1 WHERE F01 = ? FOR UPDATE"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (!resultSet.next())
                    {
                        throw new LoginException("问题不存在.");
                    }
                }
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            execute(connection,
                "UPDATE T5021_1 SET F02 = ?,F03 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ? WHERE F01 = ?",
                question.getQuestionTypeID(),
                question.getPublishStatus().name(),
                serviceResource.getSession().getAccountId(),
                now,
                question.publishTime(),
                now,
                question.getQuestion(),
                question.getQuestionAnswer(),
                articleType.name(),
                id);
        }
    }
    
    @Override
    public void updateQuestionType(T5011_F02 articleType, Question questionType, int id)
        throws Throwable
    {
        if (id <= 0 || questionType == null)
        {
            return;
        }
        String imageCode = null;
        
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        UploadFile uploadFile = questionType.getImage();
        if (uploadFile != null)
        {// 保存封面图片
            imageCode = fileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), uploadFile)[0];
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM T5021 WHERE F01 = ? FOR UPDATE"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (!resultSet.next())
                    {
                        throw new LoginException("问题类型不存在.");
                    }
                }
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            // 修改记录信息
            if (StringHelper.isEmpty(imageCode))
            {
                execute(connection,
                    "UPDATE T5021 SET F02 = ?,F03 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ? WHERE F01 = ?",
                    questionType.getQuestionType(),
                    questionType.getPublishStatus().name(),
                    serviceResource.getSession().getAccountId(),
                    now,
                    questionType.publishTime(),
                    now,
                    articleType.name(),
                    id);
            }
            else
            {
                execute(connection,
                    "UPDATE T5021 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ? WHERE F01 = ?",
                    questionType.getQuestionType(),
                    questionType.getPublishStatus().name(),
                    imageCode,
                    serviceResource.getSession().getAccountId(),
                    now,
                    questionType.publishTime(),
                    now,
                    articleType.name(),
                    id);
                
            }
        }
    }
    
    @Override
    public QuestionTypeRecord getQuestionType(int id, T5011_F02 articleType)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<QuestionTypeRecord>()
            {
                
                @Override
                public QuestionTypeRecord parse(ResultSet rs)
                    throws SQLException
                {
                    QuestionTypeRecord qtr = null;
                    if (rs.next())
                    {
                        qtr = new QuestionTypeRecord();
                        qtr.id = rs.getInt(1);
                        qtr.questionType = rs.getString(2);
                        qtr.publishStatus = ArticlePublishStatus.valueOf(rs.getString(3));
                        qtr.imageCode = rs.getString(4);
                    }
                    return qtr;
                }
                
            }, "SELECT F01,F02,F03,F04 FROM T5021 WHERE F01 = ? AND F09 = ? ", id, articleType.name());
        }
    }
    
    @Override
    public QuestionRecord getQuestion(int id, T5011_F02 articleType)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<QuestionRecord>()
            {
                
                @Override
                public QuestionRecord parse(ResultSet rs)
                    throws SQLException
                {
                    QuestionRecord qtr = null;
                    if (rs.next())
                    {
                        qtr = new QuestionRecord();
                        qtr.id = rs.getInt(1);
                        qtr.questionTypeID = rs.getInt(2);
                        qtr.publishStatus = ArticlePublishStatus.valueOf(rs.getString(3));
                        qtr.question = rs.getString(4);
                        qtr.questionAnswer = rs.getString(5);
                    }
                    return qtr;
                }
                
            }, "SELECT F01,F02,F03,F09,F10 FROM T5021_1 WHERE F01 = ? AND F11 = ?", id, articleType.name());
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
                connection.prepareStatement("SELECT F01,F02,F03,F04 FROM T5021 WHERE F09 = ? AND F03 = ?"))
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
                        record.publishStatus = ArticlePublishStatus.valueOf(resultSet.getString(3));
                        record.imageCode = resultSet.getString(4);
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
    public void deleteQuestion(int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            // 删除文章记录
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5021_1 WHERE F01 = ?"))
            {
                for (int id : ids)
                {
                    if (id <= 0)
                    {
                        continue;
                    }
                    pstmt.setInt(1, id);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        }
        
    }
    
    @Override
    public void deleteQuestionType(int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 删除问题类型
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5021_1 WHERE F02 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                // 删除问题
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5021 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error("ArticleManageImpl.deleteQuestionType() error", e);
                throw e;
            }
        }
    }
    
    public boolean isExistQuestionType(T5011_F02 articleType, String questionType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM T5021 WHERE F09 = ? AND F02 = ?"))
            {
                pstmt.setString(1, articleType.name());
                pstmt.setString(2, questionType);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public T5010 getArticleTypeByCode(String code)
        throws Throwable
    {
        T5010 t5010 = new T5010();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S50.T5010 WHERE  F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, code);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t5010.F01 = resultSet.getInt(1);
                        t5010.F02 = resultSet.getString(2);
                        t5010.F03 = resultSet.getString(3);
                        t5010.F04 = T5010_F04.parse(resultSet.getString(4));
                        t5010.F05 = resultSet.getInt(5);
                    }
                }
            }
        }
        return t5010;
    }
    
    @Override
    public T5010[] getArticleCategoryAll(int parentId)
        throws Throwable
    {
        ArrayList<T5010> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07 FROM S50.T5010 WHERE  F05 = ?"))
            {
                pstmt.setInt(1, parentId);
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
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
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
    public void updateT5010(T5010 t5010)
        throws Throwable
    {
        if (t5010 == null)
        {
            return;
        }
        if (StringHelper.isEmpty(t5010.F03))
        {
            throw new ParameterException("文章类别名称不能为空！");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S50.T5010 SET F03  = ?, F04  = ? WHERE F02 = ? "))
            {
                pstmt.setString(1, t5010.F03);
                pstmt.setString(2, t5010.F04.name());
                pstmt.setString(3, t5010.F02);
                pstmt.execute();
            }
        }
        
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
    public void updateBatchArticleOrder(String ids, int order)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S50.T5011 SET F04  = ? WHERE F01 IN (");
        List<Object> params = new ArrayList<Object>();
        params.add(order);
        sql.append(getBatchId(ids, params));
        sql.append(")");
        try (Connection connection = getConnection())
        {
            execute(connection, sql.toString(), params.toArray());
        }
    }
    
    @Override
    public int addFile(T5011_3 t5011_3, PartFile part)
        throws Throwable
    {
        String imageCode = null;
        try (FileStore fileStore = serviceResource.getResource(FileStore.class))
        {
            UploadFile uploadFile = part;
            if (uploadFile != null)
            {// 保存文件路径
                imageCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), uploadFile)[0];
            }
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int id =
                    insert(connection,
                        "INSERT INTO S50.T5011_3 SET F02=?, F03 = ?,F04 = ?,F05 = ?",
                        t5011_3.F02,
                        imageCode,
                        t5011_3.F04,
                        t5011_3.F05);
                serviceResource.commit(connection);
                return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    @Override
    public T5011_3 getFileInfo(int Id)
        throws Throwable
    {
        T5011_3 info = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S50.T5011_3 WHERE F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, Id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info = new T5011_3();
                        info.F01 = resultSet.getInt(1);
                        info.F02 = resultSet.getInt(2);
                        info.F03 = resultSet.getString(3);
                        info.F04 = resultSet.getString(4);
                        info.F05 = resultSet.getString(5);
                        info.F06 = resultSet.getInt(6);
                    }
                    return info;
                }
            }
        }
    }
    
    @Override
    public void updateFile(T5011_3 t5011_3, PartFile part)
        throws Throwable
    {
        String imageCode = null;
        try (FileStore fileStore = serviceResource.getResource(FileStore.class))
        {
            UploadFile uploadFile = part;
            if (uploadFile != null)
            {// 保存文件路径
                imageCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), uploadFile)[0];
            }
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S50.T5011_3 SET F03 = ?, F04 = ?, F05 = ? WHERE F02 = ?"))
                {
                    pstmt.setString(1, imageCode);
                    pstmt.setString(2, t5011_3.F04);
                    pstmt.setString(3, t5011_3.F05);
                    pstmt.setInt(4, t5011_3.F02);
                    pstmt.execute();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public PagingResult<OperateReport> searchOperateReport(OperateReportQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T5011.F01 F01,T5011.F05 F02,T5011.F06 F03,T5011.F09 F04,T5011.F11 F05,T5011.F12 F06,T5011.F13 F07,T7110.F04 F08,(SELECT T5011_3.F03 FROM S50.T5011_3 WHERE T5011_3.F02=T5011.F01 LIMIT 1) F09 FROM S50.T5011 INNER JOIN S71.T7110 ON T5011.F10 = T7110.F01 INNER JOIN S50.T5010 ON T5011.F02 = T5010.F01");
        sql.append(" WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            T5011_F02 articleType = query.getType();
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (articleType != null)
            {
                sql.append(" AND T5010.F02 = ?");
                parameters.add(articleType.name());
            }
            ArticlePublishStatus publishStatus = query.getPublishStatus();
            if (publishStatus != null)
            {
                sql.append(" AND T5011.F05 = ?");
                parameters.add(publishStatus.name());
            }
            String string = query.getTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5011.F06 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            string = query.getPublisherName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            Timestamp timestamp = query.getLastUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F13) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getLastUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5011.F13) <= ?");
                parameters.add(timestamp);
            }
            
            
        }
        sql.append(" ORDER BY T5011.F12 DESC,T5011.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<OperateReport>()
            {
                
                @Override
                public OperateReport[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<OperateReport> list = null;
                    OperateReport news = null;
                    while (rs.next())
                    {
                        news = new OperateReport();
                        news.id = rs.getInt(1);
                        news.publishStatus = ArticlePublishStatus.valueOf(rs.getString(2));
                        news.title = rs.getString(3);
                        news.imageCode = rs.getString(4);
                        news.createtime = rs.getTimestamp(5);
                        news.publishTime = rs.getTimestamp(6);
                        news.updateTime = rs.getTimestamp(7);
                        news.publisherName = rs.getString(8);
                        news.pdfUrl = rs.getString(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(news);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new OperateReport[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public void update(int id, T5011_F02 articleType, Article article, T5011_3 t5011_3, PartFile part)
        throws Throwable
    {
        if (id <= 0 || article == null)
        {
            return;
        }
        String title;
        String source;
        String imageCode = null;
        String content = null;
        ArticlePublishStatus publishStatus = article.getPublishStatus();
        {
            // 校验
            title = article.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("标题不能为空.");
            }
            if (title.length() > 30)
            {
                throw new ParameterException("标题不能超过30个字.");
            }
            source = article.getSource();
            if (!StringHelper.isEmpty(source) && source.length() > 50)
            {
                throw new ParameterException("文章来源不能超过50个字.");
            }
            if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
            {
                content = article.getContent();
                if (StringHelper.isEmpty(content) && !T5011_F02.BYJDFWB.getChineseName().equals(title))
                {
                    throw new ParameterException("文章内容不能为空");
                }
            }
        }
        
        if (articleType == T5011_F02.YYBG)
        {
            if (null != t5011_3 && t5011_3.F06 == 0)
            {
                throw new ParameterException("年份不能为空");
            }
        }
        FileStore imageFileStore = serviceResource.getResource(FileStore.class);
        UploadFile imageUploadFile = article.getImage();
        if (imageUploadFile != null)
        {// 保存封面图片
            imageCode = imageFileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), imageUploadFile)[0];
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM T5011 WHERE F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            throw new LoginException("文章不存在.");
                        }
                    }
                }
                // 修改记录信息
                if (StringHelper.isEmpty(imageCode))
                {
                    execute(connection,
                        "UPDATE T5011 SET F04 = ?, F06 = ?, F07 = ?, F08  =? ,F12=?, F13 = CURRENT_TIMESTAMP() WHERE F01 = ?",
                        article.getSortIndex(),
                        title,
                        source,
                        article.getSummary(),
                        article.publishTime(),
                        id);
                }
                else
                {
                    execute(connection,
                        "UPDATE T5011 SET F04 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ? ,F12=?, F13 = CURRENT_TIMESTAMP() WHERE F01 = ?",
                        article.getSortIndex(),
                        title,
                        source,
                        article.getSummary(),
                        imageCode,
                        article.publishTime(),
                        id);
                }
                if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
                {
                    // 修改内容
                    execute(connection, "UPDATE T5011_1 SET F02 = ? WHERE F01 = ?", content, id);
                }
                
                if (publishStatus != null)
                {
                    setPublishStatus(publishStatus, connection, id);
                }
                
                //附件
                String fileCode = null;
                try (FileStore fileStore = serviceResource.getResource(FileStore.class))
                {
                    UploadFile uploadFile = part;
                    if (uploadFile != null)
                    {// 保存文件路径
                        fileCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), uploadFile)[0];
                    }
                }
                if (null != t5011_3)
                {
                    if (articleType == T5011_F02.YYBG)
                    {
                    	if(!StringHelper.isEmpty(fileCode)){
                    		 execute(connection,
                                     "UPDATE S50.T5011_3 SET F03 = ?, F04 = ?, F05 = ?, F06 = ? WHERE F02 = ?",
                                     fileCode,
                                     t5011_3.F04,
                                     t5011_3.F05,
                                     t5011_3.F06,
                                     t5011_3.F02);
                    	}else{
                    		 execute(connection,
                                     "UPDATE S50.T5011_3 SET  F06 = ? WHERE F02 = ?",
                                     t5011_3.F06,
                                     t5011_3.F02);
                    	}
                       
                    }
                    else
                    {
                    	if(!StringHelper.isEmpty(fileCode)){
                            execute(connection,
                                    "UPDATE S50.T5011_3 SET F03 = ?, F04 = ?, F05 = ? WHERE F02 = ?",
                                    fileCode,
                                    t5011_3.F04,
                                    t5011_3.F05,
                                    t5011_3.F02);
                    	}
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public int add(T5011_F02 articleType, Article article, T5011_3 t5011_3, PartFile part)
        throws Throwable
    {
        if (articleType == null)
        {
            throw new ParameterException("没有指定文章类型");
        }
        String title;
        String source;
        String imageCode = null;
        String content = null;
        ArticlePublishStatus publishStatus = null;
        {
            // 校验
            title = article.getTitle();
            if (StringHelper.isEmpty(title))
            {
                throw new ParameterException("标题不能为空.");
            }
            if (title.length() > 30)
            {
                throw new ParameterException("标题不能超过30个字.");
            }
            source = article.getSource();
            if (!StringHelper.isEmpty(source) && source.length() > 50)
            {
                throw new ParameterException("文章来源不能超过50个字.");
            }
            
            publishStatus = article.getPublishStatus();
            if (publishStatus == null)
            {
                publishStatus = ArticlePublishStatus.WFB;
            }
            if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
            {
                content = article.getContent();
                if (StringHelper.isEmpty(content))
                {
                    throw new ParameterException("文章内容不能为空");
                }
            }
        }
        if (articleType == T5011_F02.YYBG)
        {
            if (null != t5011_3 && t5011_3.F06 == 0)
            {
                throw new ParameterException("年份不能为空");
            }
        }
        
        FileStore imageFileStore = serviceResource.getResource(FileStore.class);
        UploadFile imageUploadFile = article.getImage();
        if (imageUploadFile != null)
        {// 保存封面图片
            imageCode = imageFileStore.upload(FileType.ARTICLE_IMAGE.ordinal(), imageUploadFile)[0];
        }
        
        //附件  pdf
        String fileCode = null;
        try (FileStore fileStore = serviceResource.getResource(FileStore.class))
        {
            UploadFile uploadFile = part;
            if (uploadFile != null)
            {// 保存文件路径
                fileCode = fileStore.upload(FileType.PERFORMANCE_REPORT_ATTACHMENT.ordinal(), uploadFile)[0];
            }
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                T5010 t5010 = getArticleTypeByCode(articleType.name());
                int id =
                    insert(connection,
                        "INSERT INTO T5011 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ?,F12 = ?",
                        t5010.F01,
                        0,
                        article.getSortIndex(),
                        publishStatus.name(),
                        title,
                        source,
                        article.getSummary(),
                        imageCode,
                        serviceResource.getSession().getAccountId(),
                        now,
                        article.publishTime());
                if (articleType != T5011_F02.SJBG && articleType != T5011_F02.YYBG)
                {
                    execute(connection, "INSERT INTO T5011_1 SET F01 = ?, F02 = ?", id, content);
                }
                if (articleType == T5011_F02.YYBG)
                {
                    insert(connection,
                        "INSERT INTO S50.T5011_3 SET F02=?, F03 = ?,F04 = ?,F05 = ?,F06 = ?",
                        id,
                        fileCode,
                        t5011_3.F04,
                        t5011_3.F05,
                        t5011_3.F06);
                }
                else
                {
                    insert(connection,
                        "INSERT INTO S50.T5011_3 SET F02=?, F03 = ?,F04 = ?,F05 = ?",
                        id,
                        fileCode,
                        t5011_3.F04,
                        t5011_3.F05);
                }
                serviceResource.commit(connection);
                return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
}
