# CAHPTER4. 스트림 소개
: SQL에서 질의를 어떻게 구현해야 할지 명시할 필요 X. 구현은 자동으로 제공.
-> 컬렉션으로 비슷한 거 구현할 수 있을까?

## 스트림이란 무엇인가?
: 데이터 컬렉션 반복을 단순화. (단순화 버전)
: 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소. <br>
-> 연속된 요소
- 컬렉션(주제: 데이터) : 시간과 공간의 복잡성과 관련된 요소 저장 및 접근 연산
- 스트림(주제: 계산) : filter, sorted, map처럼 표현 계산식.
<br><br>

-> 소스
: 데이터 제공 소스로부터 데이터를 소비. => 순서 유지됨.

-> 데이터 처리 연산
: 함수형 프로그래밍 언어에서 지원하는 연산과 데이터베이스와 비슷한 연산.

* 스트림 특징
  - 파이프라이닝 : 스트림 연산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환. 게으름, 쇼트서킷 같은 최적화를 얻을 수 O.
  - 내부 반복 : 



** 칼로리가 400보다 낮은 요리 필터링.
* 자바7
```java
// 칼로리가 400보다 낮은 음식 고르기.
List<Dish> lowCaloricDishes = new ArrayList<>();

for(Dish dish: menu){
  if(dish.getCalories() < 400) {
    lowCaloricDishes.add(dish);
  }
}

// 칼로리 비교해서 정렬.
Collections.sort(lowCaloricDishes, new Comparator<Dish>(){
  public int compare(Dish dish1, Dish dish2){
    return Integer.compare(dish1.getCalories(), dish2.getCalories());
}
});

// 이름만 뽑아서 리스트에 저장.
List<String> lowCaloricDishesName = new ArrayList<>();
for(Dish dish: lowCaloricDishes){
  lowCaloricDishesName.add(dish.getName());
}
```

* 자바 8
```java
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

List<String> lowCaloricDishesName = menu.stream()
                                        .filter(d -> d.getCalories() < 400)
                                        .sorted(comparing(Dish::getCalories)
                                        .map(Dish::getName)
                                        .collect(toList());
```

<br>

```java
List<String> lowCaloricDishesName = menu.parallelStream()
                                        .filter(d -> d.getCalories() < 400)
                                        .sorted(comparing(Dish::getCalories)
                                        .map(Dish::getName)
                                        .collect(toList());
```

filter 같은 연산은 '고수준 빌딩 블록'으로 이루어져 있으므로 특정 스레딩 모델에 제한 X. ?
-> 데이터 처리 과정을 병렬화하면 스레드와 락 걱정 X. (스트림 API 덕분) <br>

* 스트림 API 특징
  - 선언형 (더 간결하고 가독성이 좋아진다)
  - 조립할 수 있음 (유연성이 좋아진다)
  - 병렬화 (성능이 좋아진다)

<br><br>





## 스트림과 컬렉션




## 스트림 연산







