# CHAPTER. 스트림 활용
## 1. 필터링
### 1) 프레디케이트로 필터링, Filter
```java
List<Dish> vegetarianMenu = menu.stream()
                                .filter(Dish::isVegetarian)
                                .collect(toList());
```

### 2) 고유 요소 필터링, Distinct
```java
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
      .filter(i -> i % 2 == 0)
      .distinct()
      .forEach(System.out::println);
```

## 2. 스트림 슬라이싱
### 1) 프레디케이트를 이용한 슬라이싱, TAKEWHILE & DROPWHILE
#### TAKEWHILE 활용
```java
List<Dish> filteredMenu = specialMenu.stream()
                                      .filter(dish -> dish.getCalories() < 320)
                                      .collect(toList());
```
filter는 조건에 대해 다 검사하며 참인것만 다음으로 넘어가지만, <br> 
takeWhile은 조건에 대해 참이 아닐경우 바로 거기서 멈추게 된다. 

#### DROPWIHLE 활용
TAKEWHILE과 반대.<br>
프레디케이트가 거짓이 되면 그 지점에서 작업을 중단 후 남은 요소 반환. <br><br>

### 2) 스트림 축소, Limit
```java
List<Dish> filteredMenu = specialMenu.stream()
                                      .filter(dish -> dish.getCalories() > 300)
                                      .limit(3)
                                      .collect(toList());
```
프레디케이트와 일치하는 처음 세 요소를 선택 후 즉시 결과 반환.

### 3) 요소 건너뛰기, Skip
```java
List<Dish> dishes = menu.stream()
                        .filter(dish -> dish.getCalories() > 300)
                        .skip(2)
                        .collect(toList());
```

## 3. 매핑
: 특정 객체엣어 특정 데이터를 선택하는 작업. <br>

### 1) 스트림의 각 요소에 함수 적용하기, Map


### 2) 스트림 평면화, FlatMap




## 4. 검색과 매칭
### 1) 쇼트서킷, AnyMatch, AllMatach, NoneMatch

### 2) 요소 검색, FindAny


### 3) 첫번째 요소 찾기, FindFirst



## 5. 리듀싱
### 1) 요소의 합, Reduce


### 2) 최댓값과 최솟값, Reduce(Integer::max) || Reduce(Integer::min)



## 6. 숫자형 스트림
### 1) 기본형 특화 스트림
### 1-1) 숫자 스트림으로 매핑

### 1-2) 객체 스트림으로 복원하기

### 1-3) 기본값 : OptionalInt


### 2) 숫자 범위


### 3) 숫자 스트림 활용: 피타고라스 수



## 7. 스트림 만들기
### 1) 값으로 스트림 만들기

### 2) null이 될 수 있는 객체로 스트림 만들기

### 3) 배열로 스트림 만들기

### 4) 파일로 스트림 만들기

### 5) 함수로 무한 스트림 만들기기
