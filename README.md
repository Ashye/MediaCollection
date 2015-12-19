# MediaCollection
## 计划
+ 用 toolbar 菜单取代 FloatActionButton done
+ 添加去收藏功能  使用 snackbar 实现
+ 查看收藏顶详情
+ webview 刷新功能
+ 收藏项icon 最新动态 done
+ 实现自由缩放，去掉了，上滑appbar出界面功能
+ 横竖屏切换  done
+ 收藏按纽
+ 本地收藏刷新  done
+ 刷新收藏 latest 信息  done
    + 后台低优先级刷新任务管理  ？？？
+ 联网请求封装  done




## 抽象，接口，模块
### 本地存储
+ Storage<abstract>
    + SharePrefStorage<implimentation>
    + SqlStorage : 待实现


#### Storage
##### 抽象方法
+ save(String key, String value) --> boolean
+ save(String key, int value) --> boolean
+ loadString(String key) --> String
+ loadInt(String key) --> int


### 收藏管理
+ CollectionController

+ mainFrameTab
    + localCollectionListFragment
        + delete
        + localCollectionDetail
            + delete
            + add

    + onlineFragment
        + add
        + delete




## 附录

### UI-ASD(Android Support Design)
http://www.androidchina.net/2967.html


### Toolbar
http://blog.csdn.net/jdsjlzx/article/details/41441083

http://www.open-open.com/lib/view/open1431356199216.html#_label1

http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1118/2006.html

### CoordinatorLayout
http://my.oschina.net/kooeasy/blog/484593