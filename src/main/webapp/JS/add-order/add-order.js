window.onload = function() {
    getCustomers();
}

function getCustomers() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "rest/customers/");
    xhttp.onload = function() {
        var customers = JSON.parse(xhttp.responseText);
        let rows = '';
        for (let i = 0; i < customers.length; i++) {
            rows +=
                "<option value = " + customers[i].customerID + ">" +
                customers[i].name + "</option>";
        }
        console.log(rows)
        console.log(customers)
        document.getElementById("customerChoose").innerHTML = rows;
    }
    xhttp.send();
}

function addOrder() {
    let itemToInsert = {
        customer: document.getElementById('customerChoose').value,
        date: document.getElementById('date').value,
        price: document.getElementById('price').value
    };
    let itemToInsertJson = JSON.stringify(itemToInsert);
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "rest/orders/");
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.send(itemToInsertJson);
}