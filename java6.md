# Chapter6. 스트림으로 데이터 수집
## 컬렉터란 무엇인가?
collector 인터페이스 구현 : 스트림의 요소를 어떤 식으로 도출할 지 지정.

1) collector : collect에서 필요한 메서드를 정의해놓은 인터페이스
2) collect : 스트림의 최종 연산 메서드 중 하나

스트림에서 collect를 호출하면 내부적으로 리듀싱 연산이 수행.
<br><br>

### Collectors에서 제공하는 메서드의 기능 (미리 정의된 컬렉터)
#### 1. 스트림 요소를 하나의 값으로 리듀스하고 요약 <br>

##### 1-1. 개수 세기, Collectors.counting()
```java
import static java.util.stream.Collectors.*;

long howManyDishes = menu.stream.collect(counting());
```
<br>

##### 1-2. 최대 최소 검색, Collectors.maxBy() OR Collectors.minBy()
: 스트림 요소를 비교하는 데 사용할 Comparator를 인수로 받는다. <br>
```java
Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
```
<br>

##### 1-3. 요약 연산
: 객체를 int로 매핑하는 함수를 인수로 받는다. <br>
- 합 계산 <br>
  : Collectors.summingInt, summingLong, summingDouble <br><br>
- 평균값 계산 <br>
  : Collectors.averagingInt, averagingLong, averagingDouble <br><br>
- 요소 수, 합계, 평균, 최대 최소값 계산 <br>
  : summarizingInt(IntSummaryStatistics), summarizingLong(LongSummaryStatistics), summarizingDouble(DoubleSummaryStatistics) <br><br>
<br>

```java
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
//IntSummaryStatistics{count=9, sum=4300, min=120, average=477.77778, max=800}
```
<br>

##### 1-4. 문자열 연결, Collectors.joining()
: 내부적으로 StringBuilder를 이용해서 문자열을 하나로 만든다. <br>
만약 Dish class 내부에 toString 메서드가 포함되어있다면 map은 생략 가능. <br>
```java
String shortMenu = menu.stream().map(Dish::getName).collect(joining());
String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
```
<br>

#### 1-5. 범용 리듀싱 요약 연산
```java
Optional<Dish> mostCalorieDish = menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2);
```


#### 2. 요소 그룹화
* 요리 종류에 따라 그룹화 하려면?
```java
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
```
<br><br>
* 요리 종류와 칼로리 두 기준으로 동시에 그룹화 하려면?
```java
Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
// {OTHER=[french fries, pizza], MEAT=[pork, beef]}
```

<br><br>
* 사라진 FISH 되살리기
: filter를 사용하면, 필터 프리디케이트를 만족시키는 FISH 종류 요리가 없기 때문에 해당 키 자체가 사라진다. <br>
```java
Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
// {OTHER=[french fries, pizza], MEAT=[pork, beef], FISH=[]}

Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
```

<br><br>
*두 가지 이상의 기준을 동시에 적용해서 그룹화 하려면?
```java
Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(groupingBy(Dish::getType,
                                                                                                groupingBy(dish -> {
                                                                                                    if(dish.getCalories() <= 400)
                                                                                                        return CaloricLevel.DIET;
                                                                                                    else if(dish.getCalories() <= 700)
                                                                                                        return CaloricLevel.NORMAL; else return CaloricLevel.FAT;
                                                                                                })
                                                                      )                             
);

//{
//MEAT = {DIET=[chicken], NORMAL=[beef], FAT=[prok]},
//FISH = {DIET=[prawns], NORMAL=[salmon]},
//OTHER = {DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}
//}
```

<br><br>
* 서브그룹으로 데이터 수집
* 1) 개수 세기
```java
Map<Dish.Type, Long> typesCount = menu.stream().collect(
                      groupingBy(Dish::getType, counting()));

//{MEAT=3, FISH=2, OTHER=4}
```

<br><br>
* 2) 서브그룹에서 가장 칼로리가 높은 요리 찾기
: CollectingAndThen 메서드는 Collecting을 진행한 후 그 결과로 메서드를 하나 더 호출 할 수 있게 해줌.
```java
Map<Dish.Type, Dish> mostCaloricByType = menu.stream()
                                              .collect(groupingBy(Dish::getType,
                                                      collectingAndThen(
                                                            maxBy(comparingInt(Dish::getCalories)),       //결과 : Optional[pork]
                                                      Optional::get)));                                   //결과 : pork

//{FISH=salmon, OTHER=pizza, MEAT=pork}
```

### 3. 요소 분할
: 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능. 키 형식은 Boolean. <br><br>

```java
Map<Boolean, List<Dish>> partitionedMenu =
              menu.stream().collect(partitioningBy(Dish::isVegetarian));

// {false=[pork, beef, chicken, prawns, salmon],
//  true=[french fries, rice, season fruit, pizza]}


List<Dish> vegetarianDishes = partitionedMenu.get(true);

List<Dish> vegetarianDishes =
            menu.stream().filter(Dish::isVegetarian).collect(toList()); // 위와 동일한 결과
```
<br><br>


## 4. Collector 인터페이스
```
public interface Collector<T, A, R> {
    Supplier<A> supplier();
    BiConsumer<A, T> accumulator();
    BinaryOperator<A> combiner();
    Function<A, R> finisher();
    Set<Characteristics> characteristics();
}
```
<br>
[시그니처] <br>
- T는 수집될 스트림 항목의 제네릭 형식.
- A는 누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체의 형식.
- R은 수집 연산 결과 객체의 형식.
<br><br>

[메서드]
* supplier 메서드 : 새로운 결과 컨테이너 만들기. 
```java
public Supplier<List<T>> supplier() {
    return ArrayList::new;
}
```
<br>

* accumulator 메서드 : 결과 컨테이너에 요소 추가하기.
-> 리듀싱 연산을 수행하는 함수를 반환.
```java
public BiConsumer<List<T>, T> accumulator() {
    return List::add;
}
```
<br>

* finisher 메서드 : 최종 변환값을 결과 컨테이너로 적용하기.
-> 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 호출할 함수를 반환.
```java
public Function<List<T>, List<T>> finisher() {
    return Function.identity();
}
```
<br>
![image](https://github.com/21900646/Bit/assets/69943167/237582fb-4fa6-4827-8058-a10feec3e44d)



* combiner 메서드 : 두 결과 컨테이너 병합. <br>
-> 스트림의 서로 다른 서브파트를 병렬로 처리할 때 이 결과를 어떻게 처리할 지 정의.
```java
public BinaryOperator<List<T>> combiner() {
    return (list1, list2) -> {
        list1.addAll(list2);
        return list1;
    };
}
```
<br>

* Characteristics 메서드 <br>
1) UNORDERED - 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 X.
2) CONCURRENT - 병렬 리듀싱 수행 가능.
3) IDENTITY_FINISH - 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용 가능.
<br>


## 6. 커스텀 컬렉터를 구현해서 성능 개선하기
**1단계. collector 클래스 시그니처 정의**
기존의 Collector 인터페이스 정의 <br>
= public interface Collector<T, A, R>
<br><br>
public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>
<br><br>

다섯 메서드 구현!
**2단계. 리듀싱 연산 구현**
```java
public Supplier<Map<Boolean, List<Integer>>> supplier(){
  return () -> new HashMap<Boolean, List<Integer>>() {{
    put(true, new ArrayList<Integer>());
    put(false, new ArrayList<Integer>());
  }};
}

public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator(){
  return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
    acc.get(isPrime(acc.get(true), candidate))
        .add(candidate);
  };
}
```
<br>
3단계. 병렬 실행할 수 있는 컬렉터 만들기(가능하다면)
```java
public BinaryOperator<Map<Boolean, List<Integer>>> combiner(){
  return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
    map1.get(true).addAll(map2.get(true));
    map1.get(false).addAll(map2.get(false));
    return map1;
  };
}
```
4단계. finisher 메서드와 컬렉터의 characteristics 메서드
```java
public Function<Map<Boolean, List<Integer>>,
                Map<Boolean, List<Integer>>> finisher(){
    return Function.identity();

public Set<Characteristics> characteristics(){
  return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
}
```


