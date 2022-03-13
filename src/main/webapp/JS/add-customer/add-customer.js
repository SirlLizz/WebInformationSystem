function addCustomer() {
    var name = document.getElementById('name').value;
    console.log(name)
    let itemToInsert = {
        name: document.getElementById('name').value,
        phoneNumber: document.getElementById('telephone').value,
        address: document.getElementById('address').value
    };
    console.log(itemToInsert)
    let itemToInsertJson = JSON.stringify(itemToInsert);
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "rest/customers");
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.send(itemToInsertJson);
}