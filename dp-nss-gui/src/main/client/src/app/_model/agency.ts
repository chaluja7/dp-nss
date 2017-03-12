export class Agency {
    id: string;
    name: string;
    url: string;
    phone: string;
    canBeDeleted: boolean;

    constructor(id?: string, name?: string) {
        this.id = id;
        this.name = name;
    }
}