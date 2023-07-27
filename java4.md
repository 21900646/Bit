# CAHPTER4. 스트림 소개
: SQL에서 질의를 어떻게 구현해야 할지 명시할 필요 X. 구현은 자동으로 제공.
-> 컬렉션으로 비슷한 거 구현할 수 있을까?

<br><br>

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
  - 내부 반복
 
* 스트림의 기능데이터 처리 연산
  * filter: 특정 요소를 제외
  * map: 한 요소를 다른 요소로 변환, 추출
  * limit: 스트림 크기를 축소
  * collect: 스트림을 다른 형식으로 변환



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

<br><br><br>


## 스트림과 컬렉션
### 공통점 
: 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스 제공. -> 순차적으로 값에 접근.

<br><br>
### 차이점 1 : 데이터를 언제 계산하느냐

- 컬렉션 : 모든 값을 메모리에 저장하는 자료구조. (적극적 생성)
-> 모든 요소는 컬렉션에 추가하기 전에 계산되야 함.
  
- 스트림 : 요청할 때만 요소를 계산 (게으른 생성)
: 생산자와 소비자 관계를 형성.
: 게으르게 만들어지는 컬렉션.

스트림도 한 번만 탐색 가능. -> 탐색된 스트림의 요소는 소비.
다시 탐색을 하려면 새로운 스트림을 만들어야 함.

<br><br>
### 차이점 2 : 데이터 반복 처리 방법

- 컬렉션 : 외부 반복 (사용자가 직접 요소를 반복)
- 스트림 : 내부 반복 (알아서 처리 후 결과 스트림값을 어딘가에 저장)
-> 반복을 숨겨주는 연산 리스트가 미리 정의되어 있어야 함.

<br><br><br>

## 스트림 연산

```java
List<Stirng> names = menu.stream() // 스트림 open
		.filter(dish -> dish.getCalories > 300) // 중간 연산 시작
		.map(Dish::getname)
		.limit(3) // 중간 연산 끝, short-circuit
		.collect(toList()); // 종단 연산
```

### 중간 연산
다른 스트림을 반환. -> 여러 중간 연산을 연결해 질의를 생성 가능.
특징 : 단말 연산은 스트림 파이프라인에 실행하기 전까지는 아무 연산도 수행 X. "게으르다(lazy)"
((중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다.))

[게으른 특성 -> 최적화 효과]
1. 쇼트서킷
: 모든 연산을 다 해보기 전에 조건을 만족하면 추가적인 불필요한 연산은 하지 않는다. 위의 예시에서는 limit 연산이 쇼트 서킷 연산에 해당된다. 3개의 결과를 얻은 후 앞선 filter와 map연산은 더 이상 수행할 필요가 없어 빠르게 최종 연산을 수행한다.


2. 루프 퓨전
: 둘 이상의 연산이 합쳐 하나의 연산으로 처리됨

### 최종 연산
: 스트림 파이프라인에서 결과를 도출. 스트림 외의 결과를 반환하는 연산을 말한다.

<br><br><br>

## 스트림 이용하기
- 질의를 수행할 데이터 소스
- 스트림 파이프라인을 구성할 중간 연산 연결
- 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
<br><br><br>
## 정리
- 스트림은 소스에서 추출된 연속 요소로, 데이터 처리 연산을 지원.
- 스트림은 내부 반복을 지원. (내부 반복은 filter, map, sorted 등의 연산으로 반복을 추상화)
- 스트림은 중간 연산과 최종 연산이 있다.
- 중간 연산은 filter와 map처럼 스트림을 반환하면서 다른 연산과 연결되는 연산.
  -> 이를 이용해 파이프라인을 구성할 수 있지만, 어떤 결과도 생성 X.
- 최종 연산은 forEach나 count처럼 스트림 파이프라인을 처리해서 결과를 반환.
- 스트림의 요소는 요청할 때 게으르게 계산됨.