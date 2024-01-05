
export type Transaction = {
    id: number;
    userId: number;
    amount: number;
    transactionType: string;
    transactionDate: Date;
}

export type Address = {
    country: string;
    city: string;
    street: string;
    streetNumber: string;
    houseNumber: string;
    zipCode: string;
}

export type User = {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string | Date;
    phoneNumber: string;
    accountNumber: string;
    address: Address;
}

