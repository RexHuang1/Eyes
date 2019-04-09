package com.dev.rexhuang.eyes.model;

import java.util.List;

/**
 * *  created by RexHuang
 * *  on 2019/4/9
 */
public class HomePicEntity {
    private String nextPageUrl;
    private List<IssueListEntity> issueList;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public List<IssueListEntity> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<IssueListEntity> issueList) {
        this.issueList = issueList;
    }

    public static class IssueListEntity{
        private long date;
        private long publishTime;
        private String type;
        private int count;
        private List<ItemListEntity> itemList;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ItemListEntity> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListEntity> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListEntity{
            private String type;
            private DataEntity data;
            private String image;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public DataEntity getData() {
                return data;
            }

            public void setData(DataEntity data) {
                this.data = data;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public static class DataEntity{
                private int id;
                private long date;
                private int idx;
                private String title;
                private String description;
                private String image;
                private String category;
                private int duration;
                private String playUrl;
                private String text;
                private ConsumptionEntity consumption;
                private Object promotion;
                private Object waterMarks;
                private ProviderEntity provider;
                private Object author;
                private Object adTrack;
                private Object sharedAdTrack;
                private Object favoritedAdTrack;
                private Object webAdTrack;

                private CoverEntity cover;
                private WebUrlEntity webUrl;
                private Object campaign;

                private List<PlayInfoEntity> playInfo;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public long getDate() {
                    return date;
                }

                public int getIdx() {
                    return idx;
                }

                public void setIdx(int idx) {
                    this.idx = idx;
                }

                public void setDate(long date) {
                    this.date = date;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public String getPlayUrl() {
                    return playUrl;
                }

                public void setPlayUrl(String playUrl) {
                    this.playUrl = playUrl;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public ConsumptionEntity getConsumption() {
                    return consumption;
                }

                public void setConsumption(ConsumptionEntity consumption) {
                    this.consumption = consumption;
                }

                public Object getPromotion() {
                    return promotion;
                }

                public void setPromotion(Object promotion) {
                    this.promotion = promotion;
                }

                public Object getWaterMarks() {
                    return waterMarks;
                }

                public void setWaterMarks(Object waterMarks) {
                    this.waterMarks = waterMarks;
                }

                public ProviderEntity getProvider() {
                    return provider;
                }

                public void setProvider(ProviderEntity provider) {
                    this.provider = provider;
                }

                public Object getAuthor() {
                    return author;
                }

                public void setAuthor(Object author) {
                    this.author = author;
                }

                public Object getAdTrack() {
                    return adTrack;
                }

                public void setAdTrack(Object adTrack) {
                    this.adTrack = adTrack;
                }

                public Object getSharedAdTrack() {
                    return sharedAdTrack;
                }

                public void setSharedAdTrack(Object sharedAdTrack) {
                    this.sharedAdTrack = sharedAdTrack;
                }

                public Object getFavoritedAdTrack() {
                    return favoritedAdTrack;
                }

                public void setFavoritedAdTrack(Object favoritedAdTrack) {
                    this.favoritedAdTrack = favoritedAdTrack;
                }

                public Object getWebAdTrack() {
                    return webAdTrack;
                }

                public void setWebAdTrack(Object webAdTrack) {
                    this.webAdTrack = webAdTrack;
                }

                public CoverEntity getCover() {
                    return cover;
                }

                public void setCover(CoverEntity cover) {
                    this.cover = cover;
                }

                public WebUrlEntity getWebUrl() {
                    return webUrl;
                }

                public void setWebUrl(WebUrlEntity webUrl) {
                    this.webUrl = webUrl;
                }

                public Object getCampaign() {
                    return campaign;
                }

                public void setCampaign(Object campaign) {
                    this.campaign = campaign;
                }

                public List<PlayInfoEntity> getPlayInfo() {
                    return playInfo;
                }

                public void setPlayInfo(List<PlayInfoEntity> playInfo) {
                    this.playInfo = playInfo;
                }

                public static class ConsumptionEntity {
                    private int collectionCount;
                    private int shareCount;
                    private int playCount;
                    private int replyCount;

                    public int getCollectionCount() {
                        return collectionCount;
                    }

                    public void setCollectionCount(int collectionCount) {
                        this.collectionCount = collectionCount;
                    }

                    public int getShareCount() {
                        return shareCount;
                    }

                    public void setShareCount(int shareCount) {
                        this.shareCount = shareCount;
                    }

                    public int getPlayCount() {
                        return playCount;
                    }

                    public void setPlayCount(int playCount) {
                        this.playCount = playCount;
                    }

                    public int getReplyCount() {
                        return replyCount;
                    }

                    public void setReplyCount(int replyCount) {
                        this.replyCount = replyCount;
                    }
                }

                public static class ProviderEntity{
                    private String name;
                    private String alias;
                    private String icon;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAlias() {
                        return alias;
                    }

                    public void setAlias(String alias) {
                        this.alias = alias;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }
                }

                public static class CoverEntity{
                    private String feed;
                    private String detail;
                    private String blurred;
                    private String sharing;

                    public String getFeed() {
                        return feed;
                    }

                    public void setFeed(String feed) {
                        this.feed = feed;
                    }

                    public String getDetail() {
                        return detail;
                    }

                    public void setDetail(String detail) {
                        this.detail = detail;
                    }

                    public String getBlurred() {
                        return blurred;
                    }

                    public void setBlurred(String blurred) {
                        this.blurred = blurred;
                    }

                    public String getSharing() {
                        return sharing;
                    }

                    public void setSharing(String sharing) {
                        this.sharing = sharing;
                    }
                }

                public static class WebUrlEntity{
                    private String raw;
                    private String forWeibo;

                    public String getRaw() {
                        return raw;
                    }

                    public void setRaw(String raw) {
                        this.raw = raw;
                    }

                    public String getForWeibo() {
                        return forWeibo;
                    }

                    public void setForWeibo(String forWeibo) {
                        this.forWeibo = forWeibo;
                    }
                }

                public static class PlayInfoEntity{
                    private int heignht;
                    private int width;
                    private String name;
                    private String type;
                    private String url;

                    public int getHeignht() {
                        return heignht;
                    }

                    public void setHeignht(int heignht) {
                        this.heignht = heignht;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }
        }
    }
}
