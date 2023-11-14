export interface UserDTO {
    type: UserType;
    name: string;
    email: string;
    publicId: string;
    token: string;
}

export enum UserType {
    ADMIN = "ADMIN",
    CLIENT = "CLIENT",
}