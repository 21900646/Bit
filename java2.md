# CHAPTER 2. 동작 파라미터화 코드 전달하기
자바 8 이전에는, 메서드를 다른 메서드로 전달할 방법 X.

동작 파라미터란? 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록.
나중에 프로그램에서 호출. 즉 실행은 나중으로 미뤄진다.
-> 변화하는 요구사항에 쉽게 적응하는 유용한 패턴임.

동작 파라미터화 패턴 : 동작을 캡슐화  ---메서드로 전달--->  메서드의 동작을 파라미터화.

강점 : 동작을 분리할 수 있다는 점. 


## 변화하는 요구사항에 대응하기
### 1. 값 파라미터화 
**1-1. 녹색 사과 필터링 -> 문제점 : 다양한 색의 필터링 불가**
```
enum Color {RED, GREEN}

public static List<Apple> filterGreenApples(List<Apple> inventory){
   List<Apple> result = new ArrayList<>();
   for(Apple apple : inventory){
      if(GREEN.equals(apple.getColor()) {
         result.add(apple);
      }
   }
   return result;
}
```
<br>

**1-2. 색을 파라미터화 -> 문제점 : 코드 중복. DRY(Don't repeat yourself)를 어기는 것.**
```
public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color){
   List<Apple> result = new ArrayList<>();
   for(Apple apple : inventory){
      if(apple.getColor().equals(color) {
         result.add(apple);
      }
   }
   return result;
}

public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight){
   List<Apple> result = new ArrayList<>();
   for(Apple apple : inventory){
      if(apple.getWeight() > weight){
         result.add(apple);
      }
   }
   return result;
}
```
<br>

**1-3. 모든 속성을 파라미터화 -> 문제점 : 유연하게 대응 불가.**
```
public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag){
   List<Apple> result = new ArrayList<>();
   for(Apple apple : inventory){
      if((flag && apple.getColor().equals(color)) ||
         (!flag && apple.getWeight() > weight){
         result.add(apple);
      }
   }
   return result;
}
```
<br>

### 2. 클래스 (동작 파라미터화)
**: 추상적 조건으로 필터링, 즉 인터페이스 파라미터화 -> 유연성 확보 !** <br><br>
***전략 디자인 패턴** : 런타임에 알고리즘을 선택하는 기법. (조건에 따라  filter가 다르게 동작)* <br>
***프레디케이트** : 참 또는 거짓을 반환하는 함수.* <br>
```
// 알고리즘 패밀리
public interface ApplePredicate{
   boolean test (Apple apple);
}

// 전략
public class AppleHeavyWeightPredicate implements ApplePredicate {
   public boolean test(Apple apple) {
      return apple.getWeight() > 150;
   }
}

public class AppleGreenColorPredicate implements ApplePredicate {
   public boolean test(Apple apple){
      return GREEN.equals(apple.getColor());
   }
}
```

```
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p){
   List<Apple> result = new ArrayList<>();
   for(Apple apple : inventory){
      if(p.test(apple)){
         result.add(apple);
      }
   }
   return result;
}

public class AppleRedAndHeavyPredicate implements ApplePredicate {
   public boolean test(Apple apple){
      return RED.equals(apple.getColor()) && apple.getWeight() > 150;      // 이 부분을 구현하기 위해 다른 코드까지 많이 써야함.
   }
}

List<Apple> redAndHeavyApples = filterApples(inventory, new AppleRedAndHeavyPredicate());
```
하지만, 인터페이스를 구현하는 여러 클래스를 정의 -> 인스턴스화해야함. <br>
메서드는 객체만 인수로 받기 때문에 test method를 ApplePredicate 객체로 싸서 전달해야함.

=> 로직과 관련 없는 코드가 많이 추가.

---
###### 여기서부턴, 복잡한 과정 간소화

이를 해결하고자 클래스 선언과 동시에 인스턴스화를 할 수 있는 "익명 클래스"라는 기법 제공.

### 3. 익명 클래스 (동작 파라미터화)
: 이름이 없는 클래스. 클래스 선언과 인스턴스화를 동시에 할 수 있음. 즉석으로 필요한 구현 만들어서 사용 가능.

```
//1
List<Apple> redApples = filterApples(inventory, new ApplePredicate(){
   public boolean test(Apple apple){
      return RED.equals(apple.getColor());        // 이 부분을 구현하기 위해 다른 코드까지 많이 써야함. 
   }
});

//2
button.setOnAction(new EventHandler<ActionEvent>(){
   public void handle(ActionEvent event){
      System.out.println("click");                 // 이 부분을 구현하기 위해 다른 코드까지 많이 써야함. 
   }
});

```

-> 하지만 이것조차 많은 공간 차지.
##### 4. 람다 (동작 파라미터화)
-> 복잡성 문제 해결 !
```
List<Apple> result = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
```

##### 5. 리스트 형식으로 추상화
: 바나나, 오렌지, 정수, 문자열 등의 리스트에 필터 메서드를 사용 O.
```
public interface Predicate<T> {
   boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p){
   List<T> result = new ArrayList<>();
   for(T e: list){
      if(p.test(e)){
         result.add(e);
      }
   }
   return result;
}

List<Apple> redApples = filter(inventory, (Apple apple) -> RED.equals(apple.getColor());
List<Integer> evenNumbers = filter(numbers, (Integer i ) -> i%2 == 0;
```


### 실전 예제 (자바 API의 많은 메서드를 다양한 동작으로 파라미터화)
1. Comparator로 정렬하기
   : 변화하는 요구사항에 대응할 수 있는 '다양한 정렬 동작'
```
//java.util.Comparator
public interface Comparator<T> {
  int compare(T o1, T o2);
}

inventory.sort(new Comparator<Apple>() {
  public int compare(Apple a1, Apple a2){
    return a1.getWeight().compareTo(a2.getWeight());
});
```
OR 
```
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
``` 
   
2. Runnable로 코드 블록 실행하기
   : 스레드에게 어떤 코드를 실행할 것인지 알려주기.
```
//java.lang.Runnable
public interface Runnable{
   void run();
}

Thread t = new Thread(new Runnable(){
   public void run(){
      System.out.println("Hello World");
   }
});
```
OR
```
Thread t = new Thread(() -> System.out.println("Hello World"));


```
   
3. Callable을 결과로 변환하기
   : Callable 인터페이스를 통해 결과를 반환하는 task를 만든다.
```
// java.util.concurrent.Callable
public interface Callable<V>{
   V call();
}

ExecutorService execcutorService = Executors.newCachedThreadPool();
Future<String> threadName = executorService.submit(new Callable<String>(){
   @Override
   public String call() throws Exception{
      return Thread.currentThread().getName();
   }
});
```
OR
```
Future<String> threadName = executorService.submit(() -> Thread.currentThread().getName());
```

4. GUI 이벤트 처리하기
   : 유저의 클릭이나 이동 동의 이벤트의 변화에 대응할 수 있는 유연한 코드 필요.
```
Button button = new Button("Send");
button.setOnAction(new EventHandler<ActionEvent>(){
   public void handle(ActionEvent event){
      label.setText("Sent!!");
   }
});
```
OR
```
button.setOnAction((ActionEvent event) -> label.setText("Sent!!"));
```
-> EventHandler가 setOnAction 메서드의 동작을 파라미터화한다.


---
### 정리
- 동작 파라미터화에서는 메서드 내부적으로 다양한 동작을 수행할 수 있도록, 코드를 메서드 인수로 전달.
- 동작 파라미터화의 장점 : 변화하는 요구사항에 더 잘 대응 가능. 엔지니어링 비용 감소.
- 익명 클래스로도 코드 어느정도 깔끔하게 가능. -> 자바 8에서는 인터페이스를 상속받아 여러 클래스 구현 안해도 됨.
- 자바 API의 많은 메서드는 다양한 동작으로 파라미터화 할 수 있음.



