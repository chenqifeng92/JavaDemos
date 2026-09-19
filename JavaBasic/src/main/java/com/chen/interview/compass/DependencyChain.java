package com.chen.interview.compass;

import java.util.*;

/**
 * “依赖链”解析器 (The "Dependency Chain" Resolver)
 * 给定一个依赖对列表 List<String[]>，其中每个内部数组为 [dependent_module, dependency]
 * （[依赖模块, 被依赖模块]），
 * 编写一个方法 List<String> getBuildOrder(List<String[]> dependencies)，
 * 返回模块的有效构建顺序。例如，如果模块 "B" 依赖于 "A"，则顺序应为 [A, B]。
 * 如果存在循环依赖，该方法应返回空列表。
 */

public class DependencyChain {

    public List<String> getBuildOrder(List<String[]> dependencies){
        //定义 图结构
        Map<String, List<String>> graph = new HashMap<>();
        //被依赖次数
        Map<String, Integer> indegree = new HashMap<>();

        // 初始化节点
        for(String[] dep : dependencies){

            String dependent = dep[0];
            String dependency = dep[1];

            graph.putIfAbsent(dependency,new ArrayList<>());
            graph.putIfAbsent(dependent,new ArrayList<>());

            indegree.putIfAbsent(dependency,0);
            indegree.putIfAbsent(dependent,0);

        }

        // 构建图

        for(String[] dep : dependencies){

            String dependent = dep[0];
            String dependency = dep[1];

            graph.get(dependency).add(dependent);
            indegree.put(dependent,indegree.get(dependent)+1);
        }

        // 把所有入度为0的节点放入梯队

        Queue<String> queue = new LinkedList<>();
        for(Map.Entry<String, Integer> entry : indegree.entrySet()){
            if(entry.getValue()==0){
                queue.offer(entry.getKey());
            }
        }

        List<String> buildOrder = new ArrayList<>();

        // 拓扑排序
        while(!queue.isEmpty()){
            String module = queue.poll();
            buildOrder.add(module);

            for(String dependent : graph.get(module)){
                indegree.put(dependent,indegree.get(dependent)-1);
                if(indegree.get(dependent)==0){
                    queue.offer(dependent);
                }
            }
        }

        // 如果排序后结果数量不等于节点数量 有环
        if(buildOrder.size() != indegree.size()){
            return Collections.emptyList();
        }

        return buildOrder;
    }

    public static void main(String[] args) {
        DependencyChain dependencyChain = new DependencyChain();
        List<String[]> deps = Arrays.asList(
                new String[]{"B","A"}, //B depends on A
                new String[]{"C","B"}, //C depends on B
                new String[]{"D","A"}  //D depends on A
        );
        System.out.println(dependencyChain.getBuildOrder(deps));
    }
}
