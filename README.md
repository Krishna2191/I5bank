# I5bank

Trust your bank

Simple Android mobile app that detects spam calls on fly by consuming bank exposed API. 
Users were given option to check the authenticity during the call by firing an API to the request bank.

Assumption or Note:  Banks should expose API with the list of TollFree or contact numbers from which they would make a call to the customer.

1) Once the I5 bank app is installed, User Should add Desired Bank that they have accounts to the I5 App.
2) Once a call is received,
    a) If the number found in the Users contact list, allow the User to attend the call
    b) If the number not present in the Contact list, the user should attend the call and ask the caller about his/her Calling bank info.
       Once the caller gives information about the bank details, I5 bank app requests the Banks API to check if the caller is calling from a valid bank provided number.
       If the API hit return "Invalid" then the user can can the call. If not, he can proceed with the bank call.
       
This applicaion is not limited for banking. This can be applied across all domains
