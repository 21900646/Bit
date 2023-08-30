# Chapter6. 스트림으로 데이터 수집
## 컬렉터란 무엇인가?
collector 인터페이스 구현 : 스트림의 요소를 어떤 식으로 도출할 지 지정.

1) collector : collect에서 필요한 메서드를 정의해놓은 인터페이스
2) collect : 스트림의 최종 연산 메서드 중 하나

** 고급 리듀싱 기능을 수행하는 컬렉터
스트림에서 collect를 호출하면 내부적으로 리듀싱 연산이 수행.


### Collectors에서 제공하는 메서드의 기능 (미리 정의된 컬렉터)
#### 1. 스트림 요소를 하나의 값으로 리듀스하고 요약 <br>

##### 1-1. 개수 세기, Collectors.counting()
```java
import static java.util.stream.Collectors.*;

long howManyDishes = menu.stream.collect(counting());
```

##### 1-2. 최대 최소 검색, Collectors.maxBy() OR Collectors.minBy()
: 스트림 요소를 비교하는 데 사용할 Comparator를 인수로 받는다. <br>
```java
Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
```
<br>

##### 1-3. 요약 연산, Collectors.summingInt()
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
//결과값은
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

//
{
MEAT = {DIET=[chicken], NORMAL=[beef], FAT=[prok]},
FISH = {DIET=[prawns], NORMAL=[salmon]},
OTHER = {DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}
}
```

### 3. 요소 분할






## 5. Collector 인터페이스

## 6. 커스텀 컬렉터를 구현해서 성능 개선하기
