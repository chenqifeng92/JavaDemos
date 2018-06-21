package com.chen.entity;

public class Emp {
    private String name;
    private int age;
    private String gender;
    private double salary;

    public Emp(String name, int age, String gender, double salary) {
        super();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return "Emp [name=" + name + ", age=" + age + ", gender=" + gender + ", salary=" + salary + "]";
    }


	/*@Override
	public boolean equals(Object obj){
		if(this==obj)
			return true;
		if(obj==null)
			return false;
		if(getClass()!=obj.getClass())
			return false;
		Emp other = (Emp)obj;
		if(name == null){
			if(other.name!=null)
				return false;
		}else if(!name.equals(other.name))
			return false;
		return true;
	}*/

    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime*result +((name == null)? 0 : name.hashCode());
        return result;
    }
}
