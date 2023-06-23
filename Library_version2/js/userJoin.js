window.onload = function() {
    // Make HTML
    MakeBirth();


    // initalization (not to show error messages)
    let idCheck = true;
    let idNotSame = false;
    let pwCheck = true;
    let pwDetailCehck = true;
    let pwSameCheck = true;
    let phoneNumCheck = true;

    let memberJoin = false;


    // Show error messages
    document.querySelector(".container__userInput").addEventListener('change', (event) => {
        if(!idCheck) document.getElementById("idError").textContent = "6자에서 20자 이내로 입력해주세요.";
        if(idCheck && !idNotSame) document.getElementById("idError").textContent = "";

        if(!pwCheck) document.getElementById("pwdError").textContent = "8자에서 20자 이내로 입력해주세요.";
        if(!pwDetailCehck) document.getElementById("pwdError").textContent = "문자, 숫자, 특수문자를 포함해주세요.";
        if(pwDetailCehck && pwCheck) document.getElementById("pwdError").textContent = "";

        if(!pwSameCheck) document.getElementById("pwdCheckError").textContent = "입력된 비밀번호가 다릅니다.";
        if(pwSameCheck) document.getElementById("pwdCheckError").textContent = "";

        if(!phoneNumCheck) document.getElementById("phoneNumError").textContent = "숫자만 입력하세요.";
        if(phoneNumCheck) document.getElementById("phoneNumError").textContent = "";
    });


    // ID validation ------------------------------------------------------------
    //ID checking (length)
    document.getElementById("id").addEventListener('change', (event) => {
        idCheck = false;

        if(event.target.value.length > 20 || event.target.value.length < 6) idCheck = false;
        else idCheck = true;
    });


    // ID checking (Not Same)
    document.getElementById("sameCheck").addEventListener('click', (event) => {
        idNotSame = userData.getInstance().getSameUser();
        if(idNotSame) document.getElementById("idError").textContent = "동일한 아이디가 이미 존재합니다. 다른 아이디를 입력해주세요.";
    });




    // PW validation -------------------------------------------------------------
    document.getElementById("pwd").addEventListener('change', (event) => {
        let numCount = false;
        let charCount = false;
        let alphaCount = false;


        // PW checking (length)
        pwCheck = false;
        if(event.target.value.length < 20 && event.target.value.length > 7) pwCheck = true;


        // PW checking (kind)
        pwDetailCehck = false;

        for(let i = 0; i<event.target.value.length; i++){
            if(event.target.value[i].charCodeAt(0) >= 48 && event.target.value[i].charCodeAt(0) <= 57){
                numCount = true;
            }
            else if(event.target.value[i].charCodeAt(0) >= 65 && event.target.value[i].charCodeAt(0) <= 122){
                alphaCount = true;
            }
            else if(event.target.value[i].charCodeAt(0) >= 33 && event.target.value[i].charCodeAt(0) <= 47){
                charCount = true;
            }
        };

        if(numCount == true && alphaCount == true && charCount == true) pwDetailCehck = true;
    });


    // PW checking (Both are same)
    document.getElementById("pwdCheck").addEventListener('change', (event) => {
        pwSameCheck = false;

        if(document.getElementById("pwdCheck").value == document.getElementById("pwd").value) pwSameCheck = true;
    });



    // Phone number validation -------------------------------------------------------------
    // Phone number checking (Only number)
    document.getElementById("phoneNum").addEventListener('change', (event) => {
        phoneNumCheck = true;

        for(let i = 0; i<event.target.value.length; i++){
            if(event.target.value[i].charCodeAt(0) < 48 || event.target.value[i].charCodeAt(0) > 57){
                phoneNumCheck = false; 
            }
        }
    });
}



function MakeBirth(){
    let day = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

    // year
    document.getElementById("birth_year").innerHTML = `<option value="">년도</option>`;

    for(let i = 1920; i<2024; i++){
        document.getElementById("birth_year").innerHTML += `
        <option>${i}년</option>
    `;
    }

    // month
    document.getElementById("birth_month").innerHTML = `<option value="">월</option>`;

    for(let i = 1; i<13; i++){
        document.getElementById("birth_month").innerHTML += `
        <option>${i}월</option>
    `;
    }

    // day
    document.getElementById("birth_day").innerHTML = `<option value="">일</option>`;
    document.getElementById("birth_month").onclick = function(){

        for(let i = 1; i < day[document.getElementById("birth_month").value[0]-1]; i++){
            document.getElementById("birth_day").innerHTML += `
            <option>${i}일</option>
        `;
        }
    }
}



// Get user data
class userData{
    static #instance = null;
    static getInstance(){
        if(this.#instance == null){
            this.#instance = new userData();
        }
        return this.#instance;
    }

    getUserJson(){
        let userDataContainer = null;

        $.ajax({
            async: false,
            url: 'json/member.json',
            dataType: 'json',
            success: function(data){
                userDataContainer = data;
            },
            error: function(error){
                console.error(error);
            }
        });

        return userDataContainer;
    }

    getSameUser(){
        let result = false;
        let userInfo = userData.getInstance().getUserJson();

        userInfo.some(function(user) {
            if(user.member_id == document.getElementById("id").value) {
               result = true; // 동일한게 있다면
               return true;
            }
        });

        return result;
    }
}
