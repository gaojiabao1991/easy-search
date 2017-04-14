# easy-search
一个使用java语言实现的文本搜索引擎

## 索引设计
### 倒排索引
> 索引分为两部分：内存索引和磁盘索引。内存索引词典使用TreeMap保存，倒排记录表(docIds)使用TreeSet保存。
### 索引合并
> 内存索引用于缓存最近更新的文档，当可用内存小于一定阈值时自动合并内存索引到磁盘索引，合并使用归并算法，时间复杂度为O(m+n)，m和n分别为内存索引和磁盘索引大小。
### 索引压缩
> Delta差值压缩：对倒排列表(docIds)使用了Delta差值压缩，经测试，压缩后索引大小约为原索引50%。

## 查询设计
### TermQuery
> 用于查找含有某个词的文档
### BooleanQuery
> 用于做布尔查询，支持语法：
 
    中国 AND 人民	//取交集
	中国 OR 人民 	//取并集
	NOT 人民		//取非
	中国 AND NOT 人民
	中国 OR 人民 AND 国旗		//多个操作符时，运算按照从左到右的顺序
	( 中国 AND 人民 ) OR ( 人民 AND 国旗 )	//支持使用小括号改变运算顺序
<br/>
## 程序结构
### 数据结构类
	Index
	--invertIndex  //TreeMap<String, TreeSet<Long>> 结构，key是token，value是文档id列表
	--docMap    //HashMap<Long, Doc> 结构，key是文档id,value是文档实体类(包含文档本地保存地址)
### 数据处理类
	Indexer
	--add(List<Doc> docs)   //向索引添加文档，当可用内存小于一定阈值时，自动触发hardCommit()
	--hardCommit()  //硬提交，将内存中的invertIndex与硬盘中的invertIndex进行合并后写入到硬盘；将内存中的docMap覆盖到硬盘。

	Searcher
	--search(IQuery query)   //从索引中根据Query搜索文档

	IQuery
	-TermQuery
	-BooleanQuery
### 测试类
	ClientTest

