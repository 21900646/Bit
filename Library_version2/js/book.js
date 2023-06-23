var test = $.getJSON("../json/book.json", function(data) {
    console.log(data);
    return data;
});

window.onload = () => {
    userData.getInstance().userAllData();
    bookData.getInstance().bookAllData();
    rentalData.getInstance().rentalAllData();
    reservationData.getInstance().reservationAllData();

    userData.getInstance().searchButtonOnclickEvent();
    test1();
}

function test1() {
    console.log("테스트 함수");
    console.log("-----------------------");
    var ee = test.responseJSON;
    console.log(ee);
    console.log("-----------------------");
}


class userData {
    static #instance = null;
    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new userData();
        }

        return this.#instance;
    }

    userAllData() {
        let userAllContainer = null;

        $.ajax({
            async: false,
            url: 'json/member.json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                userAllContainer = data;
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });

        
        return userAllContainer;
    }

    // Search user id
    searchButtonOnclickEvent() {
        const searchBoxContainer = document.querySelector(".search-button");
        const rentalBoxContainer = document.querySelector(".service-rental-button");
        const reservationBoxContainer = document.querySelector(".service-reserv-button");
        const finalButtonBoxContainer = document.querySelector(".container__button");
        const borrowRentalBoxContainer = document.querySelector(".final-button");
        const asideDataBox = document.querySelector(".aside__info__userInfo");

        let bookList = document.getElementById("tagArea");

        // Press main search button
        searchBoxContainer.onclick = () => {
            let userName;

            let inputValueBox = document.querySelector(".search-input").value;
            let userAllContainer = userData.getInstance().userAllData();
            let rentalAllContainer = rentalData.getInstance().rentalAllData();
            let reservationAllContainer = reservationData.getInstance().reservationAllData();
            let bookAllContainer = bookData.getInstance().bookAllData();


            //load user name
            userAllContainer.forEach(user => {
                if (user.member_id == inputValueBox) {
                    console.log(user.member_id);
                    userName = user.name;
                    return false; //break
                }
            });

            //load user other info
            let borrowedBooks = rentalAllContainer.filter(rental => rental.member_id === inputValueBox);
            let reservationedBooks = reservationAllContainer.filter(reservation => reservation.member_id === inputValueBox);
            let maxOverdueDays;

            let penaltyDate = rentalAllContainer.filter(rental => rental.member_id === inputValueBox).map(rental => parseInt(rental.penalty_time));
            if(penaltyDate.length === 0){
                maxOverdueDays = 0;
            } else{
                maxOverdueDays = Math.max(...penaltyDate);
            }

            console.log(penaltyDate);

            asideDataBox.innerHTML = `
                <p>${userName} 님의 데이터입니다. </p>
                <h6>대출 권수: ${borrowedBooks.length}권</h6>
                <h6>예약 권수: ${reservationedBooks.length}권</h6>
                <h6>연체일: ${maxOverdueDays}일</h6>
            `;

            // Show book list
            bookList.innerHTML = null;

            //arrange by penalty time
            rentalAllContainer.sort((a, b) => {
                if (a.penalty_time < b.penalty_time) return 1;
                if (a.penalty_time > b.penalty_time) return -1;
            
                return 0;
            });

            // Show "init" rental book list
            rentalAllContainer.forEach((rental) => {
                if (rental.member_id == inputValueBox) {
                    bookAllContainer.forEach((book) => {
                        if (book.book_no == rental.book_no) {
                            if (rental.penalty_time <= 0) {
                                bookList.innerHTML += `
                                    <div class = "container__bookInfo">

                                    <div class = "bookImg">
                                        <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                    </div>

                                    <div class="book__detail">
                                        <div class="book__detail__left__side">
                                        <p>${book.book_no}</p>
                                        <p class = "title">${book.name}</p>
                                        </div>
                                        <div class="book__detail__right__side">
                                        <p>반납일 : ${rental.return_date}</p>
                                        <p class = ${rental.penalty_time}>연체일 : - </p>
                                        </div>
                                    </div>
                                    </div>
                                `;
                            } else {
                                bookList.innerHTML += `
                                    <div class = "container__bookInfo" style="border: 2px solid red; color: red">
                                    <div class = "bookImg">
                                        <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                    </div>
                                    <div class="book__detail">
                                        <div class="book__detail__left__side">
                                        <p>${book.book_no}</p>
                                        <p class = "title">${book.name}</p>
                                        </div>
                                        <div class="book__detail__right__side">
                                        <p>반납일 : ${rental.return_date}</p>
                                        <p class = "penalty__title">연체일 : ${rental.penalty_time}일 </p>
                                        </div>
                                    </div>
                                    </div>
                                `;    
                            }
                        }
                    });
                }
            });

            // Show rental book list
            rentalBoxContainer.onclick = () => {
                bookList.innerHTML = null;
                rentalAllContainer.forEach((rental) => {
                    if (rental.member_id == inputValueBox) {
                        bookAllContainer.forEach((book) => {
                            if (book.book_no == rental.book_no) {
                                if (rental.penalty_time <= 0) {
                                    bookList.innerHTML += `
                                        <div class = "container__bookInfo">
    
                                        <div class = "bookImg">
                                            <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                        </div>
    
                                        <div class="book__detail">
                                            <div class="book__detail__left__side">
                                            <p>${book.book_no}</p>
                                            <p class = "title">${book.name}</p>
                                            </div>
                                            <div class="book__detail__right__side">
                                            <p>반납일 : ${rental.return_date}</p>
                                            <p class = ${rental.penalty_time}>연체일 : - </p>
                                            </div>
                                        </div>
                                        </div>
                                    `;
                                } else {
                                    bookList.innerHTML += `
                                        <div class = "container__bookInfo" style="border: 2px solid red; color: red">
                                        <div class = "bookImg">
                                            <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                        </div>
                                        <div class="book__detail">
                                            <div class="book__detail__left__side">
                                            <p>${book.book_no}</p>
                                            <p class = "title">${book.name}</p>
                                            </div>
                                            <div class="book__detail__right__side">
                                            <p>반납일 : ${rental.return_date}</p>
                                            <p class = "penalty__title">연체일 : ${rental.penalty_time}일 </p>
                                            </div>
                                        </div>
                                        </div>
                                    `;    
                                }
                            }
                        });
                    }
                    finalButtonBoxContainer.innerHTML = `
                    <button class="final-button">대출 / 반납하기</button>
                `;
                });

                let reborrowBoxContainer = document.querySelector(".final-button");
            
                // Show rental popup
                reborrowBoxContainer.onclick = () => {
                    console.log("re");
                    
                    var popupContainer = document.querySelector('.popup-container');
                    var head = document.querySelector(".popup-head");
                    var body = document.querySelector(".popup-body");
                    var footer = document.querySelector(".button-group-footer__popup")

                    let userPopupAllContainer = userData.getInstance().userAllData();
                    let rentalPopupAllContainer = rentalData.getInstance().rentalAllData();
                    let reservationPopupAllContainer = reservationData.getInstance().reservationAllData();
                    let bookPopupAllContainer = bookData.getInstance().bookAllData();
                
                    popupContainer.classList.add("show-popup-container");

                    head.innerHTML = `
                        <div class = "container__search__popup">
                            <input class="search-input-popup" type="text" name="" placeholder="Enter bookCode ..">
                            <button class="search-button-popup">Search</button>
                        </div>
                    `;

                    let searchPopupButtonContainer = document.querySelector(".search-button-popup");
                    

                    //search
                    searchPopupButtonContainer.onclick = () => {
                        let returnTrue = false;
                        let inputPopupValueBox = document.querySelector(".search-input-popup").value;
                        console.log("팝업 인풋 밸류: " + inputPopupValueBox);

                        rentalPopupAllContainer.forEach((rental) => {
                            if (rental.book_no == inputPopupValueBox) {
                                returnTrue = true;
                                bookPopupAllContainer.forEach((book) => {
                                    if (book.book_no == rental.book_no) {
                                        body.innerHTML = `
                                            <div id = "tagAreaPopup">
                                                <div class = "container__bookInfo__popup">
                            
                                                    <div class = "bookImg__popup">
                                                        <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                                    </div>
                    
                                                    <div class="book__detail__popup">
                                                        <div class="book__detail__left__side__popup">
                                                            <p>${book.book_no}</p>
                                                            <p class = "title">${book.name}</p>
                                                        </div>
                                                        <div class="book__detail__right__side__popup">
                                                        <button class="popup-return-button">반납하기</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        `;
                                    }
                                });
                            }
                        });

                        if(!returnTrue){
                            bookPopupAllContainer.forEach((book) => {
                                if (book.book_no == inputPopupValueBox) {
                                    body.innerHTML = `
                                        <div id = "tagAreaPopup">
                                            <div class = "container__bookInfo__popup">
                        
                                                <div class = "bookImg__popup">
                                                    <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                                </div>
                        
                                                    <div class="book__detail__popup">
                                                        <div class="book__detail__left__side__popup">
                                                            <p>${book.book_no}</p>
                                                            <p class = "title">${book.name}</p>
                                                        </div>
                                                        <div class="book__detail__right__side__popup">
                                                        <button class="popup-borrow-button">대출하기</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        `;
                                    }
                                
                                });
                                let BorrowButtonContainer = document.querySelector('.popup-borrow-button');
                                BorrowButtonContainer.onclick = () => {
                                    if (confirm("해당 도서를 대출하시겠습니까?")) {
                                        // 사용자가 확인을 선택한 경우
                                        // 반납 로직을 처리하는 코드를 추가하세요
                                        console.log("도서 대출 처리");
                                    } else {
                                        // 사용자가 취소를 선택한 경우
                                        console.log("도서 대출 취소");
                                    }
                                };
                        }

                        let returnButtonContainer = document.querySelector('.popup-return-button');
                        returnButtonContainer.onclick = () => {
                            if (confirm("해당 도서를 반납하시겠습니까?")) {
                                // 사용자가 확인을 선택한 경우
                                // 반납 로직을 처리하는 코드를 추가하세요
                                console.log("도서 반납 처리");
                            } else {
                                // 사용자가 취소를 선택한 경우
                                console.log("도서 반납 취소");
                            }
                        };

                    }

                    footer.innerHTML = `
                        <div class="button-group-footer__popup">
                            <button class="close-button">닫기</button>
                        </div>
                    `;
                    popupCloseComponent.getInstance().closeButtonOnclickEvent();
                }

            }
            
            // Show reservation book list
            reservationBoxContainer.onclick = () => {
                bookList.innerHTML = null;
                reservationAllContainer.forEach((reservation) => {
                    if (reservation.member_id == inputValueBox) {
                        bookAllContainer.forEach((book) => {
                            if (book.book_no == reservation.book_no) {
                                bookList.innerHTML += `
                                    <div class = "container__bookInfo">

                                        <div class = "bookImg">
                                            <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                        </div>

                                        <div class="book__detail">
                                            <div class="book__detail__left__side">
                                            <p>${book.book_no}</p>
                                            <p class = "title">${book.name}</p>
                                            </div>
                                            <div class="book__detail__right__side">
                                            <p>예약일 : ${reservation.reservated_at}</p>
                                            </div>
                                            <div>
                                                <button class="cancel-reservation">예약 취소</button>
                                            </div>
                                        </div>
                                    </div>
                                `;
                                let cancelBox = document.querySelector(".cancel-reservation");

                                cancelBox.onclick = () => {
                                        if (confirm("해당 도서를 예약 취소하시겠습니까?")) {
                                            // 사용자가 확인을 선택한 경우
                                            // 반납 로직을 처리하는 코드를 추가하세요
                                            console.log("도서 예약 취소 처리");
                                        } else {
                                            // 사용자가 취소를 선택한 경우
                                            console.log("도서 예약 취소 실패");
                                        }
                                    };
                                }
                            
                        });
                    }
                    finalButtonBoxContainer.innerHTML = `
                        <button class="reservation__button">예약하기</button>
                    `;
                });
                
                //Show reservation popup
                let reservationPopupButtonContainer = document.querySelector(".reservation__button");
                
                reservationPopupButtonContainer.onclick = () => {
                    var popupContainer = document.querySelector('.popup-container');
                    var head = document.querySelector(".popup-head");
                    var body = document.querySelector(".popup-body");
                    var footer = document.querySelector(".button-group-footer__popup")
    
                    let userPopupAllContainer = userData.getInstance().userAllData();
                    let rentalPopupAllContainer = rentalData.getInstance().rentalAllData();
                    let reservationPopupAllContainer = reservationData.getInstance().reservationAllData();
                    let bookPopupAllContainer = bookData.getInstance().bookAllData();
                
                    popupContainer.classList.add("show-popup-container");
    
                    head.innerHTML = `
                        <div class = "container__search__popup">
                            <input class="search-input2-popup" type="text" name="" placeholder="Enter bookCode ..">
                            <button class="search-button2-popup">Search</button>
                        </div>
                    `;
                    
                    let searchPopupButtonContainer = document.querySelector(".search-button2-popup");

                    //search
                    searchPopupButtonContainer.onclick = () => {
                        let inputPopupValueBox = document.querySelector(".search-input2-popup").value;
                        console.log("팝업 인풋 밸류: ");

    
                        rentalPopupAllContainer.forEach((rental) => {
                            if (rental.book_no == inputPopupValueBox) {
                                bookPopupAllContainer.forEach((book) => {
                                    if (book.book_no == rental.book_no) {
                                        body.innerHTML = `
                                            <div id = "tagAreaPopup">
                                                <div class = "container__bookInfo__popup">
                            
                                                    <div class = "bookImg__popup">
                                                        <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                                    </div>
                    
                                                    <div class="book__detail__popup">
                                                        <div class="book__detail__left__side__popup">
                                                            <p>${book.book_no}</p>
                                                            <p class = "title">${book.name}</p>
                                                        </div>
                                                        <div class="book__detail__right__side__popup">
                                                           <button class="popup-reservation-button">예약하기</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        `;
                                    }
                                });
                            }
                        });
    
                        let returnButtonContainer = document.querySelector('.popup-reservation-button');
                        returnButtonContainer.onclick = () => {
                            if (confirm("해당 도서를 예약하시겠습니까?")) {
                                // 사용자가 확인을 선택한 경우
                                // 반납 로직을 처리하는 코드를 추가하세요
                                console.log("도서 예약 처리");
                            } else {
                                // 사용자가 취소를 선택한 경우
                                console.log("도서 예약 취소");
                            }
                        };
    
                    }
    
                    footer.innerHTML = `
                        <div class="button-group-footer__popup">
                            <button class="close-button">닫기</button>
                        </div>
                    `;
                    popupCloseComponent.getInstance().closeButtonOnclickEvent();
                }
            }

            // Show rental popup
            borrowRentalBoxContainer.onclick = () => {
                var popupContainer = document.querySelector('.popup-container');
                var head = document.querySelector(".popup-head");
                var body = document.querySelector(".popup-body");
                var footer = document.querySelector(".button-group-footer__popup")

                let userPopupAllContainer = userData.getInstance().userAllData();
                let rentalPopupAllContainer = rentalData.getInstance().rentalAllData();
                let reservationPopupAllContainer = reservationData.getInstance().reservationAllData();
                let bookPopupAllContainer = bookData.getInstance().bookAllData();
            
                popupContainer.classList.add("show-popup-container");

                head.innerHTML = `
                    <div class = "container__search__popup">
                        <input class="search-input-popup" type="text" name="" placeholder="Enter bookCode ..">
                        <button class="search-button-popup">Search</button>
                    </div>
                `;

                let searchPopupButtonContainer = document.querySelector(".search-button-popup");
                

                //search
                searchPopupButtonContainer.onclick = () => {
                    let returnTrue = false;
                    let inputPopupValueBox = document.querySelector(".search-input-popup").value;
                    console.log("팝업 인풋 밸류: " + inputPopupValueBox);

                    rentalPopupAllContainer.forEach((rental) => {
                        if (rental.book_no == inputPopupValueBox) {
                            returnTrue = true;
                            bookPopupAllContainer.forEach((book) => {
                                if (book.book_no == rental.book_no) {
                                    body.innerHTML = `
                                        <div id = "tagAreaPopup">
                                            <div class = "container__bookInfo__popup">
                        
                                                <div class = "bookImg__popup">
                                                    <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                                </div>
                
                                                <div class="book__detail__popup">
                                                    <div class="book__detail__left__side__popup">
                                                        <p>${book.book_no}</p>
                                                        <p class = "title">${book.name}</p>
                                                    </div>
                                                    <div class="book__detail__right__side__popup">
                                                       <button class="popup-return-button">반납하기</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    `;
                                }
                            });
                        }
                    });

                    if(!returnTrue){
                        bookPopupAllContainer.forEach((book) => {
                            if (book.book_no == inputPopupValueBox) {
                                body.innerHTML = `
                                    <div id = "tagAreaPopup">
                                        <div class = "container__bookInfo__popup">
                    
                                            <div class = "bookImg__popup">
                                                <img src=${book.img} alt="" width="57px" style="margin-left:7px;">
                                            </div>
                    
                                                <div class="book__detail__popup">
                                                    <div class="book__detail__left__side__popup">
                                                        <p>${book.book_no}</p>
                                                        <p class = "title">${book.name}</p>
                                                    </div>
                                                    <div class="book__detail__right__side__popup">
                                                       <button class="popup-borrow-button">대출하기</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    `;
                                }
                            
                            });
                            let BorrowButtonContainer = document.querySelector('.popup-borrow-button');
                            BorrowButtonContainer.onclick = () => {
                                if (confirm("해당 도서를 대출하시겠습니까?")) {
                                    // 사용자가 확인을 선택한 경우
                                    // 반납 로직을 처리하는 코드를 추가하세요
                                    console.log("도서 대출 처리");
                                } else {
                                    // 사용자가 취소를 선택한 경우
                                    console.log("도서 대출 취소");
                                }
                            };
                    }

                    let returnButtonContainer = document.querySelector('.popup-return-button');
                    returnButtonContainer.onclick = () => {
                        if (confirm("해당 도서를 반납하시겠습니까?")) {
                            // 사용자가 확인을 선택한 경우
                            // 반납 로직을 처리하는 코드를 추가하세요
                            console.log("도서 반납 처리");
                        } else {
                            // 사용자가 취소를 선택한 경우
                            console.log("도서 반납 취소");
                        }
                    };

                }

                footer.innerHTML = `
                    <div class="button-group-footer__popup">
                        <button class="close-button">닫기</button>
                    </div>
                `;
                popupCloseComponent.getInstance().closeButtonOnclickEvent();
            }
        };
    }
}

class popupCloseComponent {
    static #instance = null;
    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new popupCloseComponent();
        }
        return this.#instance;
    }

    closeButtonOnclickEvent() {
        var popupContainer = document.querySelector('.popup-container');
        var closeButtonBox = document.querySelector('.close-button');

        closeButtonBox.onclick = () => {
            popupContainer.classList.remove('show-popup-container');
        }
    }
}

class bookData {
    static #instance = null;
    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new bookData();
        }

        return this.#instance;
    }

    bookAllData() {
        let bookAllContainer = null;

        $.ajax({
            async: false,
            url: 'json/book.json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                bookAllContainer = data;
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });

        return bookAllContainer;
    }
}

class rentalData {
    static #instance = null;
    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new rentalData();
        }

        return this.#instance;
    }

    rentalAllData() {
        let rentalAllContainer = null;

        $.ajax({
            async: false,
            url: 'json/rental.json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                rentalAllContainer = data;
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });

        return rentalAllContainer;
    }
}

class reservationData {
    static #instance = null;
    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new reservationData();
        }

        return this.#instance;
    }

    reservationAllData() {
        let reservationAllContainer = null;

        $.ajax({
            async: false,
            url: 'json/reservation.json',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                reservationAllContainer = data;
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });

        return reservationAllContainer;
    }
}