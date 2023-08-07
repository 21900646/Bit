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

### 2) 스트림 축소, Limit

### 3) 요소 건너뛰기, Skip




## 3. 매핑
### 1) 스트림의 각 요소에 함수 적용하기, MAp

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
