-- 创建一个id主键自增，带表注释的workers表
CREATE TABLE workers (id INT(10)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20) COMMENT '姓名',
	salary DOUBLE(10,2) COMMENT '工资，精确到小数点后两位',
	age INT(10) COMMENT '年龄');
-- 插入数据
INSERT INTO workers (name,salary,age) VALUES ('杜雯',55000.00,28);
-- 删除数据
DELETE FROM workers WHERE id=?;
-- 修改数据
UPDATE workers SET name=?,salary=?,age=? WHERE id=?;
-- 查询数据
SELECT * FROM workers;

-- 删除workers表全部数据
TRUNCATE workers;