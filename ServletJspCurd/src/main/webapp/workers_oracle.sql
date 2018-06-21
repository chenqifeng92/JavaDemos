-- 建表语句（关于如何在oracle创建一个id主键非空，自增，并且带表注释的表）
create table workers  (
   id INTEGER not null,
   name VARCHAR2(20),
   salary NUMBER(10,2),
   age INTEGER(10),
   constraint ORCL.workers primary key (id)
);

-- 设置主键也可以用

ALTER TABLE workers ADD CONSTRAINT workers PRIMARY key(id);
-- 这里是设置表注释
comment on table workers is '雇员信息表';
comment on column workers.id is '主键自增';
comment on column workers.name is '姓名';
comment on column workers.salary is '薪水';
comment on column workers.age is '年龄';
-- 创建自增序列
CREATE SEQUENCE example_sequence
INCREMENT BY 1 -- 每次加几个
START WITH 1 -- 从1开始计数
NOMAXVALUE -- 不设置最大值
NOCYCLE -- 一直累加，不循环
NOCACHE -- 不建缓冲区
-- 创建触发器
CREATE TRIGGER example_triger BEFORE
INSERT ON workers FOR EACH ROW WHEN (new.id is null)--只有在id为空时，启动该触发器生成id号
begin
select example_sequence.nextval into: new.id from dual;
end;