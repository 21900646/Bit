# CHAPTER. 스트림 활용
## 1. 필터링
### 1) 프레디케이트로 필터링, Filter
```java
List<Dish> vegetarianMenu = menu.stream()
                                .filter(Dish::isVegetarian)
                                .collect(toList());
```
<br>

### 2) 고유 요소 필터링, Distinct
```java
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
      .filter(i -> i % 2 == 0)
      .distinct()
      .forEach(System.out::println);
```
<br>

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
<br><br>
#### DROPWIHLE 활용
TAKEWHILE과 반대.<br>
프레디케이트가 거짓이 되면 그 지점에서 작업을 중단 후 남은 요소 반환. <br><br>
<br><br>
### 2) 스트림 축소, Limit
```java
List<Dish> filteredMenu = specialMenu.stream()
                                      .filter(dish -> dish.getCalories() > 300)
                                      .limit(3)
                                      .collect(toList());
```
프레디케이트와 일치하는 처음 세 요소를 선택 후 즉시 결과 반환.
<br><br>
### 3) 요소 건너뛰기, Skip
```java
List<Dish> dishes = menu.stream()
                        .filter(dish -> dish.getCalories() > 300)
                        .skip(2)
                        .collect(toList());
```
<br><br>
## 3. 매핑
: 특정 객체엣어 특정 데이터를 선택하는 작업. <br>
<br><br>
### 1) 스트림의 각 요소에 함수 적용하기, Map
```java
List<Integer> dishNameLengths = menu.stream()
                                    .map(Dish::getName)
                                    .map(String::length)
                                    .collect(toList());
```
<br><br>
### 2) 스트림 평면화, FlatMap
: map(Arrays::stream)과는 다르게 하나의 평면화된 스트림을 반환. <br>
1. 일반적 -> 결과 : Stream<String[]>

```java
words.stream()                      #결과 : Stream<String>
    .map(word -> word.split(""))    #결과 : Stream<String[]>
    .distinct()                     #결과 : Stream<String[]>
    .collect(toList());             #결과 : List<String[]>
```
<br>
<br>
2. map과 Arrays.stream 활용 -> 결과 : List<Stream<String>>

```java
words.stream() 
    .map(word -> word.split(""))
    .map(Arrays::stream)    
    .distinct()  
    .collect(toList()); 
```
<br><br>

3. flatMap 사용

```java
words.stream()                      #결과 : Stream<String>
    .map(word -> word.split(""))    #결과 : Stream<String[]>
    .flatMap(Arrays::stream)        #결과 : Stream<String>
    .distinct()                     #결과 : Stream<String>
    .collect(toList());             #결과 : List<String>
```
<br><br>
---
## 4. 검색과 매칭
### 1) 쇼트서킷, AnyMatch, AllMatach, NoneMatch
- anyMactch : 프레디케이트가 적어도 한 요소가 있다면 true
- allMatch : 프레디케이트가 모두 일치한다면 true
- nonMatch : 프레디케이트와 일치하는 요소가 없다면 true
```java
boolean isHealthy = menu.stream()
                        .nonMatch(d -> d.getCalories() >= 1000);
```
** limit도 쇼트서킷 연산임. <br><br>

### 2) 임의의 요소 반환, FindAny
: 가장 먼저 찾은 요소. 결과값이 실행할 때마다 달라짐. <br>
: 병렬성 때문에 요소 반환 순서가 상관없다면 제약이 적은 FindAny 사용. <br>
(병렬 실행에서는 첫 번쨰 요소를 찾기 어려움) <br>

```java
Optional<Dish> dish = menu.stream()
                          .filter(Dish::isVegetarian)
                          .findAny()                       # Optional<Dish> 변환
                          .ifPresent(dish -> System.out.println(dish.getName());
```
<br>

** Optional<T>란, null 버그를 피할 수 있도록 <br>
값의 존재나 부재 여부를 표현하는 컨테이너 클래스. <br><br>

* isPresent() <br>
: Optional이 값을 포함하면 true, 없으면 false. <br><br>
* ifPresent(Consumer<T> block)  <br>
: 값이 있으면 주어진 블록 실행. T형식의 인수를 받으면 void를 반환. <br><br>
* T get()  <br>
: 값이 존재하면 값을 반환, 없으면 NoSuchElementException. <br><br>
* T orElse(T other)  <br>
: 값이 있으면 값을 반환, 없으면 기본값을 반환. <br><br>

### 3) 스트림에서 첫번째 요소 찾기, FindFirst
```java
List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                                                           .map(n -> n*n)
                                                           .filter(n -> n % 3 == 0)
                                                           .findFirst(); //9
```
<br><br>

## 5. 리듀싱
: Integer 같은 결과가 나올 때까지 스트림의 모든 요소를 반복적으로 처리해야함. <br>
-> 모든 스트림 요소를 처리해서 값으로 도출해야 함. (= 리듀싱 연산, 폴드(함수형 프로그래밍 용어)) <br><br>


### 1) 요소의 합, Reduce
```java
int sum = numbers.stream().reduce(0, (a, b) -> a + b);

# Stream에 아무 요소가 없다면 합계를 반환할 수 없기 때문.
Optional<Integer> sum = numbers.stream().reduce((a, b) -> a + b);
```
누적값 + 스트림에서 소비한 값 <br><br>


### 2) 최댓값과 최솟값, Reduce(Integer::max) || Reduce(Integer::min)
```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
Optional<Integer> min = numbers.stream().reduce(Integer::min);
```
<br><br>
** 맵 리듀스 패턴
```java
int count = menu.stream()
                .map(d -> 1)
                .reduce(0, (a, b) -> a + b);
```
---
<br><br>
## 6. 숫자형 스트림
```java
int calories = menu.stream()
                   .map(Dish::getCalories)
                   .reduce(0, Integer::sum);
```
-> 배경 : 합계 계산 전에 Integer를 기본형으로 언박싱하는, 박싱 비용이 존재. <br>
직접 호출할 수 있는 방법은 없는가?

### 1) 기본형 특화 스트림
: 박싱 비용을 피할 수 있도록 InteStream, DoubleStream 등을 제공. <br><br>

### 1-1) 숫자 스트림으로 매핑
```java
int calories = menu.stream()
                   .mapToInt(Dish::getCalories)       # IntStream으로 반환.
                   .sum();
```

### 1-2) 객체 스트림으로 복원하기

[배경] <br>
IntStream의 map 연산은 'int를 인수로 받아서 int를 반환하는 람다'를 인수로 받는다. <br>
정수가 아닌 Dish와 같은 다른 값을 반환하고 싶다면?

```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
Stream<Integer> stream = intStream.boxed();        # 숫자 스트림 -> 스트림
```
<br><br>

### 1-3) 기본값 : OptionalInt
IntStream에서 최댓값을 찾을 때, 0이라는 기본값으로 인해 <br>
스트림에 요소가 없는 상황과 실제 최댓값이 0인 상황을 구별 X. <br>

```java
OptionalInt maxcalories = menu.stream()
                              .mapToInt(Dish::getCalories)
                              .max();

int max = maxCalories.orElse(1);  # 값이 없을 때 기본 최댓값을 명시적으로 설정 가능.
```
<br><br>

### 2) 숫자 범위, range와 rangeClosed
IntStream과 LongStream에서 제공하는 메서드. <br>
range는 시작값과 종료값이 결과에 포함 X. <br>
```java
IntStream evenNumbers = IntStream.rangeClose(1, 100);
                                  .filter(n -> n % 2 == 0);
```
<br><br>
### 3) 숫자 스트림 활용: 피타고라스 수
```java
Stream<double[]> pythagoreanTriples2 = IntStream.rangeClose(1, 100).boxed()
                                                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a*a + b*b)})
                                                .filter(t -> t[2] % 1 == 0));
```
<br><br>
---
<br>

## 7. 스트림 만들기
### 1) 값으로 스트림 만들기, Stream.of
```java
Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
```
** Stream<String> emptyStream = Stream.empty();


### 2) null이 될 수 있는 객체로 스트림 만들기, Stream.ofNullable
```java
Stream<String> stream = Stream.of("Modern", "Java", "In", "Action")
                              .flatMap(key -> Stream.ofNullable(System.getPropertyes(key)));
```

### 3) 배열로 스트림 만들기, Arrays.stream
```java
int[] numbers - {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum();
```

### 4) 파일로 스트림 만들기
```java
long uniqueWords = 0;
try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
                            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                                               .distinct()  
                                               .count();
} catch(IOException e){

}
```
** 스트림은 자원을 자동으로 해제할 수 있는 AutoCloseable이므로 try-finally가 필요 X. <br><br>

### 5) 함수로 무한 스트림 만들기, Stream.iterate와 Stream.generate
** 무한 스트림이란? 크기가 고정되지 않은 스트림. (=언바운드 스트림)<br><br>
* iterate 메서드
```java
[iterate 메서드로 생성 후, 제어방법]
// # 1
Stream.iterate(0, n-> n + 2)
      .limit(10)                           # 제어
      .forEach(System.out::println);

// # 2
Stream.iterate(0, n -> n + 2, n -> n + 4)  # 제어
      .forEach(System.out::println);

// # 3
Stream.iterate(0, n-> n + 2)
      .takeWhile(n -> n + 4)               # 제어
      .forEach(System.out::println); 
```
<br><br>
* generate 메서드 <br>
: 값을 연속적으로 계산X. -> Supplier<T>를 인수로 받아서 새로운 값 생성.
```java
Stream.generate(Math::random)
      .limit(5)                            # 제어
      .forEach(System.out::println);
```
