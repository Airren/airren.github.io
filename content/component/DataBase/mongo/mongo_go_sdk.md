---
title: 「Mongo」Mongo golang sdk
subtitle: 'Mongo golang 使用文档'
author: Airren
catalog: true
header-img: ''
tags:
  - Mongo
date: 2020-08-17 10:42:00
---



## mongo 基本数据类型







| Type      |                                                   |                                                              |
| --------- | ------------------------------------------------- | ------------------------------------------------------------ |
| `bosun.E` | `type E struct { Key string  Value interface{} }` | E represents a BSON element for a D. It is usually used inside a D. |
| `bosun.D` | `type D []E`                                      | D is an ordered representation of a BSON document. This type should be used when the order of the elements matters, // such as MongoDB command documents. If the order of the elements does not matter, an M should be used instead. // // Example usage: // //        bson.D{{"foo", "bar"}, {"hello", "world"}, {"pi", 3.14159}} |
| `bosun.M` | `type M map[string]interface{}`                   | // M is an unordered representation of a BSON document. This type should be used when the order of the elements does not // matter. This type is handled as a regular map[string]interface{} when encoding and decoding. Elements will be // serialized in an undefined, random order. If the order of the elements matters, a D should be used instead. // // Example usage: // //        bson.M{"foo": "bar", "hello": "world", "pi": 3.14159}. |
| `bosun.A` | `type A []interface{}`                            | // An A is an ordered representation of a BSON array. // // Example usage: // //        bson.A{"bar", "world", 3.14159, bson.D{{"qux", 12345}}} |



设置某个字段的值

```go
setValue := bson.E{Key: "$set", Value: bson.D{{"end_at", alert.CreatedAt}}})
          ? bson.E{Key: "$set", Value: bson.D{{"end_at", alert.CreatedAt}}})
	update = append(update, bson.E{Key: "$inc", Value: bson.D{bson.E{Key:"count", Value: 1}}})
```











### filter

```go
filter = make(bson.D, 0)
filter = 
```





### $or

```mongo
{"key1":1,"$or":[{"key2":2},{"key3":2}]
```



```go
filter:= bson.D{
  {"key1",1},
  {"$or":[]interface{
    bson.M{"key2":2},
    bson.M{"key3":2},
  }}
}
```

The query should be equivalent to the following in the mongo console

```sql
db.mycollection.find{{"key1":1,"$or":[{"key2":2},{"key3":2}]}}
```

if you'd rather wish to use unordered maps, it would be like this

```go
pipeline := bson.M{
  "key1":1;
  "$or":[]interface{
    bson.M{"key2":2},
    bson.M{"key3":2},
  }
}
```





other example

```go
findQuery := bson.M{
  "key1":1,
}

orQuery:= []bson.M{}
orQuery= append(orQuery, bson.M{"key2":2}, bson.M{"key3":2})

findquery["$or"] = orQuery
result := []interface{}
err := mongo.DB.C("collectionName").find(findQuery).All(&result)
```



