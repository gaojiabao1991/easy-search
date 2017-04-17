# easy-search
一个使用java语言实现的文本搜索引擎。

## 索引设计
### 倒排索引
> 索引分为两部分：内存索引和磁盘索引。内存索引词典使用TreeMap保存，倒排记录表(docIds)使用TreeSet保存。
### 索引合并
> 内存索引用于缓存最近更新的文档，当可用内存小于一定阈值或手动调用commit时将合并内存索引到磁盘索引，合并使用归并算法，时间复杂度为O(m+n)，m和n分别为内存索引和磁盘索引大小。
### 索引压缩
> Delta差值压缩：对倒排列表(docIds)使用了Delta差值压缩，经测试，压缩后索引大小约为原索引50%。
### 多搜索域
> 对每一个Doc,可以有多个搜索域Field，搜索域可选多种类型：如IntField、StringField、TextField，根据不同的域类型，会自动采取不同的索引策略，每个域对应一个倒排索引记录。

## 查询设计
### TermQuery
> 用于查找含有某个词的文档
### BooleanQuery
> 用于做布尔查询，支持语法：
 
    content:中国 AND content:人民	//取交集
	content:中国 OR content:人民 	//取并集
	NOT content:人民		//取非
	content:中国 AND NOT content:人民
	content:中国 OR content:人民 AND content:国旗		//多个操作符时，运算按照从左到右的顺序
	( content:中国 AND content:人民 ) OR ( content:人民 AND content:国旗 )	//支持使用小括号改变运算顺序

## 程序结构
> 格式说明：单横线(如-invertIndexCollection代表实例变量)，双横线(如--add(List<Doc> docs)代表实例方法)，-sub:（如-sub:TermQuery）代表子类/接口实现类。
### 数据结构类
	Index
	-invertIndexCollection  //倒排索引集合，一个索引中可以有多个倒排索引，对应多个搜索域
	-docMap    //HashMap<Long, Doc> 结构，key是文档id,value是文档实体类(包含文档本地保存地址)。用于通过文档id获取文档。docMap每次程序初始化会全部读到内存里。
### 数据处理类
	Indexer
	-fieldIndexers  //保存每个Field的FieldIndexer，Indexer在索引Doc时，会把Doc中的每个Field的索引工作分发给对应的FieldIndexer。
	--add(List<Doc> docs)   //向索引添加文档，当可用内存小于一定阈值时，自动触发hardCommit()
	--hardCommit()  //硬提交，将内存中的invertIndex与硬盘中的invertIndex进行合并后写入到硬盘；将内存中的docMap覆盖到硬盘。

	Searcher
	--search(IQuery query)   //从索引中根据Query搜索文档

	IQuery
	-sub:TermQuery  //通过单个词进行搜索
	-sub:BooleanQuery  //通过布尔表达式进行搜索
### 测试类
	TermQueryTest
	BooleanQueryTest
	

