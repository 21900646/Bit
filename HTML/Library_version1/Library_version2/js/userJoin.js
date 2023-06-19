window.onload = function() {
    document.getElementById("id").addEventListener('change', (event) => {
        if(event.target.value.length > 20 || event.target.value.length < 6){
            document.getElementById("idError").textContent = "6자에서 20자 이내로 입력해주세요.";
        }else{
            document.getElementById("idError").textContent = null;
        }
    });

    document.getElementById("pwd").addEventListener('change', (event) => {
        if(event.target.value.length > 20 || event.target.value.length < 8){
            document.getElementById("pwdError").textContent = "8자에서 20자 이내로 입력해주세요.";
        }
        else{
            document.getElementById("pwdError").textContent = null;
            let numCount = false;
            let charCount = false;
            let alphaCount = false;
            let splitPwd = event.target.value.split('');
            console.log(splitPwd);

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

            if(numCount == true && alphaCount == true && charCount == true){
                document.getElementById("pwdError").textContent = null;
            }else{
                document.getElementById("pwdError").textContent = "문자, 숫자, 특수문자를 포함해주세요.";
            }
        }
    });

    document.getElementById("id").addEventListener('change', (event) => {
        if(event.target.value.length > 20 || event.target.value.length < 6){
            document.getElementById("pwdCheckError").textContent = "6자에서 20자 이내로 입력해주세요.";
        }else{
            document.getElementById("pwdCheckError").textContent = null;
        }
    });
}