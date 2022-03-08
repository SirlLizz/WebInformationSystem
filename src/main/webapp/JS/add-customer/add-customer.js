function addCustomer() {
    var itemToInsert = {
        name: document.getElementById('name').value,
        telephone: document.getElementById('telephone').value,
        address: document.getElementById('address').value
    };
    var itemToInsertJson = JSON.stringify(itemToInsert);
    var xhttp = new XMLHttpRequest();
    xhttp.open('POST', 'rest/customers');
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send(itemToInsertJson);
}