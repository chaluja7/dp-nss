export class LoggedUser {
    id: number;
    username: string;
    token: string;
    roles: string[];
    isAdmin: boolean;
    passwordChangeRequired: boolean;
}