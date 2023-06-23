var test = $.getJSON("../json/book.json", function(data) {
    console.log(data);
    return data;
});


window.onload = () => {
    test1();
}

function test1() {
    console.log(test.responseJSON);


}