export interface IAttachmentFile {
    id?: number;
    name?: string;
    temporaryToken?: string;
    type?: string;
    size?: number;
    content?: any[];
}

export class AttachmentFile implements IAttachmentFile {
    constructor(
        public id?: number,
        public name?: string,
        public temporaryToken?: string,
        public type?: string,
        public size?: number,
        public content?: any[]
    ) {}
}
