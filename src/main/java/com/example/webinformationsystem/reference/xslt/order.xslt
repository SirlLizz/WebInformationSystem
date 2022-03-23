<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <body style="font-size:12pt;background-color:#EEEEEE">
        <h1 style="font-size:20pt;color:#FF0000">Customer List</h1>
        <xsl:for-each select="orders/order">
            <ul>
                <li>
                    Id: <xsl:value-of select="id"/>
                </li>
                <li>
                    Customer:
                    <ul>
                        <li>
                            Id: <xsl:value-of select="customer/id"/>
                        </li>
                        <li>
                            Name: <xsl:value-of select="customer/name"/>
                        </li>
                        <li>
                            Telephone: <xsl:value-of select="customer/phoneNumber"/>
                        </li>
                        <li>
                            Address: <xsl:value-of select="customer/address"/>
                        </li>
                    </ul>
                </li>
                <li>
                    Date: <xsl:value-of select="date"/>
                </li>
                <li>
                    Price: <xsl:value-of select="price"/>
                </li>
            </ul>
        </xsl:for-each>
    </body>
</html>