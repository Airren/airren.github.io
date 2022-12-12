# 数据库

## 关系型数据库
- 基于关系代数理论
  缺点： 表结构不直观，实现复杂速，速度慢
  优点： 健壮性高，社区庞大


Product
| product_id | product_name | category_id | price   |
| ---------- | ------------ | ----------- | ------- |
| 4          | toyota       | 2           | 100000  |
| 3          | prosche      | 2           | 1000000 |
| 2          | addidas      | 2           | 500     |
| 1          | nike         |             | 600     |

category
| category_id | category_name |
| ----------- | ------------- |
| 2           | automobile    |
| 1           | shoes         |

```sql
select * from product join category;  -- 结果为笛卡尔积 8条记录

select * from product p join category c on p.category_id=c.category_id; --  按照id相等去连接， 忽略id 为空的记录  内连接 null的数据不会显示


select * from product p left join category c on p.category_id=c.category_id; -- 左外连接， 左边为主表


select p.category_id,category_name ,count(1) as total from product p left join category c on p.category_id=c.category_id group by p.category_id; -- count 聚合函数

select p.category_id,category_name ,MIN(price) as total from product p left join category c on p.category_id=c.category_id group by p.category_id; -- min 聚合函数


select * from product p left join (
    select p.category_id,category_name ,MIN(price) as min_price
    from product p left join category c
        on p.category_id=c.category_id group by p.category_id) as cat_min
on p.category_id = cat_min.category_id
where p.price = cat_min.min_price;
/*
 获取某个品类中价格最低的商品
 */
```

## 事务
**ACID**
- Atomicity  原子性
- Consistency 一致性
- Isolation   独立性
- Durability  持久性
  
**事务的隔离级别**
- Read Uncommited  
- Read Committed
- Repeatable Read
- Serializable

![](img/2019-10-05-13-31-42.png)
![](img/2019-10-05-13-31-28.png)

`For Update` 对查询的数加锁 ## 悲观锁
![](img/2019-10-05-13-34-03.png)

## 乐观锁 
使用版本校验的方法保证事务的独立性
![](img/2019-10-05-13-36-27.png)
Sql的返回值是影响了多少行

- 读取数据，记录timestamp
- 修改数据
- 检查和提交数据 

## 数据库例题
可以用来程序调优
- 改善数据访问方式以提高缓存命中率
- 使用数据库连接池替代直接的数据库访问
- 使用迭代来代替递归
- 合并多个远程调用批量发送
- 共享冗余数据提高访问效率（不可变对象）
