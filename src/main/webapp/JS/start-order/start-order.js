window.onload = function() {
    selectAllItems();
}

function selectAllItems() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "rest/orders/");
	xhttp.onload = function() {
        var orders = JSON.parse(xhttp.responseText);
        var rows = "<tr>" +
            "<td>" + 'ID' + "</td>" +
            "<td>" + 'Customer' + "</td>" +
            "<td>" + 'Date' + "</td>" +
            "<td>" + 'Price' + "</td>" +
            "</tr>";
        for (i = 0; i < orders.length; i++) {
            rows +=
                "<tr>" +
                "<td>" + orders[i].orderID + "</td>" +
                "<td>" + orders[i].customer + "</td>" +
                "<td>" + orders[i].orderDate + "</td>" +
                "<td>" + orders[i].orderPrice + "</td>" +
                "</tr>";
        }
        console.log(rows)
        console.log(orders)
        document.getElementById("orderTable").innerHTML = rows;
    }
	xhttp.send();
}

function changeOrder() {
    var id = document.getElementById('updateOrderChoose').value
    var itemToUpdate = {
        customer: document.getElementById('updateCustomerChoose').value,
        orderDate: document.getElementById('updateDate').value,
        orderPrice: document.getElementById('updatePrice').value
    };
    var itemToUpdateJson = JSON.stringify(itemToUpdate);
    var xhttp = new XMLHttpRequest();
    xhttp.open('PUT', 'rest/orders/' + id);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send(itemToUpdateJson);
}

function deleteOrder() {
    var id = document.getElementById('deleteChoose').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('DELETE', 'rest/orders/' + id);
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send();
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
        document.getElementById("updateCustomerChoose").innerHTML = rows;
    }
    xhttp.send();
}

function getOrdersToUpdate() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "rest/orders/");
    xhttp.onload = function() {
        var orders = JSON.parse(xhttp.responseText);
        let rows = '';
        for (let i = 0; i < orders.length; i++) {
            rows +=
                "<option value = " + orders[i].orderID + ">" +
                orders[i].orderID + "</option>";
        }
        document.getElementById("updateOrderChoose").innerHTML = rows;
    }
    xhttp.send();
}

function getOrdersToDelete() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "rest/orders/");
    xhttp.onload = function() {
        var orders = JSON.parse(xhttp.responseText);
        let rows = '';
        for (let i = 0; i < orders.length; i++) {
            rows +=
                "<option value = " + orders[i].orderID + ">" +
                orders[i].orderID + "</option>";
        }
        document.getElementById("deleteChoose").innerHTML = rows;
    }
    xhttp.send();
}

function xsltOrder() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "rest/xmlOrder/");
    xhttp.onload = function() {
        if(xhttp.responseText == 1){
            window.open("xlst-order.html")
        }
    }
    xhttp.send();

}