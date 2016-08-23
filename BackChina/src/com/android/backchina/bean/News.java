package com.android.backchina.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.backchina.utils.StringUtils;


/**
 * 新闻实体类
 */
public class News extends Entity {
	
	public final static int NEWSTYPE_NEWS = 0x00;//0 新闻
	
	private String title;
	
	private String url;
	
	private String body;
	
	private String author;
	
	private int authorId;
	
	private int commentCount;
	
	private String pubDate;
	
	private String softwareLink;
	
	private String softwareName;
	
	private int favorite;
	
	private NewsType newsType;
	
	private List<Relative> relatives = new ArrayList<Relative>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = StringUtils.toInt(authorId, 0);
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSoftwareLink() {
		return softwareLink;
	}

	public void setSoftwareLink(String softwareLink) {
		this.softwareLink = softwareLink;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public NewsType getNewType() {
		return newsType;
	}

	public void setNewType(NewsType newType) {
		this.newsType = newType;
	}

	public List<Relative> getRelatives() {
		return relatives;
	}

	public void setRelatives(List<Relative> relatives) {
		this.relatives = relatives;
	}
	
	public class NewsType implements Serializable{
		private int type;
		private String attachment;
		private int authoruid2;
		private String eventUrl;
		
		public String getEventUrl() {
			return eventUrl;
		}
		public void setEventUrl(String eventUrl) {
			this.eventUrl = eventUrl;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getAttachment() {
			return attachment;
		}
		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
		public int getAuthoruid2() {
			return authoruid2;
		}
		public void setAuthoruid2(int authoruid2) {
			this.authoruid2 = authoruid2;
		}
	} 
	
	public class Relative implements Serializable{
		
		public String title;
		
		public String url;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	} 
}
