let userAllContainer;
let rentalAllContainer;
let reservationAllContainer;
let bookAllContainer;


window.onload = () => {
    userAllContainer = userData.getInstance().userAllData();
    rentalAllContainer = rentalData.getInstance().rentalAllData();
    reservationAllContainer = reservationData.getInstance().reservationAllData();
    bookAllContainer = bookData.getInstance().bookAllData();
    userData.getInstance().allRentalTable();
    userData.getInstance().overDueTable();
    
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

    


    //유저아이디 / 이름     / 도서코드      / 도서명    / 저자      / 반납일        / 연체일
    //userData /UserData / bookData / bookData / bookData / rentalData / retalData
    allRentalTable() {
        let overDueContainer = document.getElementById("allRental");
        let memberName = document.getElementById("container__books__tableName");
        let memberList = document.getElementById("container__books__tableContent");
        //arrange by penalty time
        //초기 화면 테이블 출력-------------------
        rentalAllContainer.sort((a, b) => {
            if (a.penalty_time < b.penalty_time) return 1;
            if (a.penalty_time > b.penalty_time) return -1;
        
            return 0;
        });

        console.log(rentalAllContainer);

        memberList.innerHTML = null;
        
        memberName.innerHTML = `
                <tr>
                    <th>회원번호</th>
                    <th>이름</th>
                    <th>도서코드</th>
                    <th>도서명</th>
                    <th>저자</th>
                    <th>반납일</th>
                    <th>연체일</th>

                    </tr>
        `;

        rentalAllContainer.forEach((rental) => {
                bookAllContainer.forEach((book) => {
                    userAllContainer.forEach((user) => {
                    if(book.book_no == rental.book_no && rental.member_id == user.member_id){
                                memberList.innerHTML += `
                            <tr>
                                <td>${rental.member_id}</td>
                                <td>${user.name}</td>
                                <td class="book-id">${rental.book_no}</td>
                                <td>${book.name}</td>
                                <td>${book.writer}</td>
                                <td>${rental.return_date}</td>
                                <td>${rental.penalty_time}</td>
                            </tr>
                            `;
                            
                        }
                    })
                })
        });
        //---------------------------------------//

        overDueContainer.onclick = () => {
            //arrange by penalty time
            rentalAllContainer.sort((a, b) => {
                if (a.penalty_time < b.penalty_time) return 1;
                if (a.penalty_time > b.penalty_time) return -1;
            
                return 0;
            });

            console.log(rentalAllContainer);

            memberList.innerHTML = null;
            
            memberName.innerHTML = `
                    <tr>
                        <th>회원번호</th>
                        <th>이름</th>
                        <th>도서코드</th>
                        <th>도서명</th>
                        <th>저자</th>
                        <th>반납일</th>
                        <th>연체일</th>

                        </tr>
            `;

            rentalAllContainer.forEach((rental) => {
                    bookAllContainer.forEach((book) => {
                        userAllContainer.forEach((user) => {
                        if(book.book_no == rental.book_no && rental.member_id == user.member_id){
                                    memberList.innerHTML += `
                                <tr>
                                    <td>${rental.member_id}</td>
                                    <td>${user.name}</td>
                                    <td class="book-id">${rental.book_no}</td>
                                    <td>${book.name}</td>
                                    <td>${book.writer}</td>
                                    <td>${rental.return_date}</td>
                                    <td>${rental.penalty_time}</td>
                                </tr>
                                `;
                                
                            }
                        })
                    })
            });

        };
    }    
    //---------------------------------

    overDueTable() {
        let overDueContainer = document.getElementById("overDueMember");
        let scheduledReturnBoxContainer = document.getElementById("scheduled_return_button");
        let memberName = document.getElementById("container__books__tableName");
        let memberList = document.getElementById("container__books__tableContent");

        overDueContainer.onclick = () => {

            //arrange by penalty time
            rentalAllContainer.sort((a, b) => {
                if (a.penalty_time < b.penalty_time) return -1;
                if (a.penalty_time > b.penalty_time) return 1;
            
                return 0;
            });

            memberList.innerHTML = null;
            
            memberName.innerHTML = `
                    <tr>
                        <th>회원번호</th>
                        <th>도서코드</th>
                        <th>도서명</th>
                        <th>저자</th>
                        <th>반납일</th>
                        <th>연체일</th>

                      </tr>
            `;

            rentalAllContainer.forEach((rental) => {
                if(rental.penalty_time > 0){
                    bookAllContainer.forEach((book) => {
                        if(book.book_no == rental.book_no){
                            memberList.innerHTML += `
                                <tr>
                                    <td>${rental.member_id}</td>
                                    <td class="book-id">${rental.book_no}</td>
                                    <td>${book.name}</td>
                                    <td>${book.writer}</td>
                                    <td>${rental.return_date}</td>
                                    <td>${rental.penalty_time}</td>
                                </tr>
                                `;
                        }
                    })
                }
            });
    //-------------------------------//
    scheduledReturnBoxContainer.onclick = () => {
        let rentalAllContainer = rentalData.getInstance().rentalAllData();
        
        memberList.innerHTML = null;

        memberName.innerHTML = `
                <tr>
                    <th>userId</th>
                    <th>도서코드</th>
                    <th>도서명</th>
                    <th>저자</th>
                    <th>반납일</th>
                  </tr>
        `;

        rentalAllContainer.forEach((rental) => {
            console.log(new Date().getDate());
            if (new Date(rental.return_date).getDate() == new Date().getDate()) {
                bookAllContainer.forEach((book) => {
                    if (book.book_no == rental.book_no) {
                        memberList.innerHTML += `
                            <tr>
                                <td>${rental.member_id}</td>
                                <td class="book-id">${rental.book_no}</td>
                                <td>${book.name}</td>
                                <td>${book.writer}</td>
                                <td>${rental.return_date}</td>
                            </tr>
                            `;
                    }
                })
            }
        });
    }
    //-------------------------------//
        };
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