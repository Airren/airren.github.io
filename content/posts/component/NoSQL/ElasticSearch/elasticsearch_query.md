---
title: 「ElasticSearch」 ElasticSearch 简单查询
subtitle: ''
author: Airren
catalog: true
header-img: ''
tags:
  - ElasticSearch
p: component/elasticsearch/elasticsearch_query
date: 2020-09-07 20:23:25
---



查询语句

|       |      |          |       |                                                              |                      |
| ----- | ---- | -------- | ----- | ------------------------------------------------------------ | -------------------- |
| query | bool | must     | exist | "exists": {         "field": "name"          }    | 判断字段是否存在     |
|       |      | must_not | match | "match": {            "tweet": "elasticsearch"        } | 匹配字符串中是否包含 |
|       |      | should   |       |                                                              |                      |
|       |      | Filter   |       |                                                              |                      |
|       |      |          |       |                                                              |                      |



## 简单查询

#### 查询某个字段是否存在或者是否为`null`

```sh
curl -H 'Content-type: application/json' -XPOST 'http://ip:9200/alert_group/_search' -d 
```

```json
{
  "query": {
    "bool": {
      "must": {    // must_not
        "exists": {
          "field": "name"  // 必须存在该字段，且该字段不为null
        }
      }
    }
  }
}
```

### 空查询（empty search）

 `{}`在功能上等价于使用 `match_all` 查询， 正如其名字一样，匹配所有文档：

```json
curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}
'
```



### Match

```json
GET /_search
{
    "query": {
        "match": {
            "tweet": "elasticsearch"
        }
    }
}

```



### 复合查询

组合多条件查询。elasticsearch提供`bool`来实现这种需求；

主要参数：
`must`：文档 `必须` 匹配这些条件才能被包含进来。
`must_not`：文档 `必须不` 匹配这些条件才能被包含进来。
`should`：如果满足这些语句中的任意语句，或的关系，`将增加 _score` ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。
`filter`：`必须 匹配`，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档。

一个 `bool` 语句 允许在你需要的时候组合其它语句，无论是 `must` 匹配、 `must_not` 匹配还是 `should` 匹配，同时它可以包含不评分的过滤器（filters）：

```json
{
    "bool": {
        "must":     { "match": { "tweet": "elasticsearch" }},
        "must_not": { "match": { "name":  "mary" }},
        "should":   { "match": { "tweet": "full text" }},
        "filter":   { "range": { "age" : { "gt" : 30 }} }
    }
}
```

了找出信件正文包含 `business opportunity` 的星标邮件，或者在收件箱正文包含 `business opportunity` 的非垃圾邮件：

shoule在与must或者filter同级时，默认是不需要满足should中的任何条件的，此时我们可以加上`minimum_should_match` 参数，来达到我们的目的，即上述代码改为：

```json
{
    "bool": {
        "must": { "match":   { "email": "business opportunity" }},
        "should": [
            { "match":       { "starred": true }},
            { "bool": {
                "must":      { "match": { "folder": "inbox" }},
                "must_not":  { "match": { "spam": true }}
            }}
        ],
        "minimum_should_match": 1
    }
}
```



## Term

精确查询

```bash
GET /forum/article/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "bool": {
          "must" : [
            {"term" : {"tag_cnt" : 1}},
            {"terms" : {"tag" : ["java"]}}
          ]
        }
      }
    }
  }
}
```



## 嵌套查询

如果doc中有个字段是Object Array， 通过其中的一个Object 查询

```json
{
  "query": {
    "nested": {
      "path": "tags",  // 字段名
      "query": {
        "bool": {
          "must": [
            {
              "term": {
                "tags.key": "k1"  // 查找tags 中包含object{key:k1,val:v1}的所有doc
              }
            },
            {
              "term": {
                "tags.val": "v1"
              }
            }
          ]
        }
      }
    }
  }
}
```



```json
{
  "query": {
    "nested": {
      "path": "tags",
      "query": {
        "bool": {
          "must": [
            {
              "term": {
                "tags.key": "$psm"
              }
            },
            {
              "terms": {
                "tags.val":[ "v1","v2"]  // 查询多个值
              }
            }
          ]
        }
      }
    }
  },
   "sort": [
    {
      "created_at": {
        "order": "desc"
      }
    }
  ]
}
```

在排序的过程中，只能使用可排序的属性进行排序。那么可以排序的属性有哪些呢？

- 数字
- 日期



嵌套查询的效率相对是比较低的，如果数据量非常大的情况下就会出现慢查询的问题



```json
//按照条件新建一个index 作为测试数据使用
POST _reindex
{
  "source": {
    "index": "usernested",
    "query": {
      "nested": {
        "path": "tags",
        "query": {
          "bool": {
            "must": [
              {
                "term": {
                  "tags.brand": "c55fd643-1333-4647-b898-fb3e5e4e6d67"
                }
              },
              {
                "term": {
                  "tags.site": "163"
                }
              }
            ]
          }
        }
      }
    }
  },
  "dest": {
    "index": "new_usernested"
  }
}
```







```json
//根据条件更新一个 nested的文档
GET usernested/_update_by_query
{
  "query": {
    "nested": {
      "path": "tags",
      "query": {
        "bool": {
          "must": [
            {
              "term": {
                "tags.brand": "c55fd643-1333-4647-b898-fb3e5e4e6d67"
              }
            },
            {
              "term": {
                "tags.site": "163"
              }
            }
          ]
        }
      }
    }
  },
  "script": {
    "inline": "for(e in ctx._source.tags){e.brand = 'test2';}" //更新nested字段
    //"inline":"ctx._source.userid = 'testid'"  //更新普通字段
  }
}
```



#### 通过`_source`字段中的include和exclude来指定返回结果包含哪些字段，排除哪些字段

```json
{
  "_source":{
    "include":[
      "policyNo",
      "policyRelationNo",
      "policyStatus"
    ],
    "exclude":[
       "salesType"
    ]
  },
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "policyRelationNo": "KR01435021"
          }
        }
      ],
      "should": [],
      "must_not": []
    }
  },
  "from": 0,
  "size": 10
}
```



```json

{
	"_shards": {
		"total": 5,
		"successful": 5,
		"failed": 0
	},
	"hits": {
		"total": 19,
		"max_score": 11.391884,
		"hits": [
			{
				"_index": "search4policy-msad-dev3_20200520000000",
				"_type": "policy-msad-dev3",
				"_id": "4407038",
				"_score": 11.391884,
				"_source": {
					"policyRelationNo": "KR01435021",
					"policyNo": "B609120319",
					"policyStatus": 11
				}
			},
			{
				"_index": "search4policy-msad-dev3_20200520000000",
				"_type": "policy-msad-dev3",
				"_id": "4407046",
				"_score": 10.713255,
				"_source": {
					"policyRelationNo": "KR01435021",
					"policyNo": "B609120323",
					"policyStatus": 11
				}
			}
		]
	},
	"took": 5,
	"timed_out": false
}

```





参考资料：

https://blog.csdn.net/weixin_33831196/article/details/85860371?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.add_param_isCf&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.add_param_isCf

https://blog.csdn.net/u012934325/article/details/106971482/

https://blog.csdn.net/qq_35393693/article/details/80143287

https://segmentfault.com/a/1190000020245240

*https://www.jianshu.com/p/c377477df7fc

https://blog.csdn.net/qq_32165041/article/details/83715134

https://www.jianshu.com/p/2abd2e344dcb

