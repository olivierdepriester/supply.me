import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document } from './document';
import { map } from 'rxjs/operators';

@Injectable()
export class DocumentService {
    constructor(private http: HttpClient) {}

    getDocuments(): Observable<Document[]> {
        return this.http.get('content/primeng/assets/data/json/documents/documents.json', { observe: 'response' }).pipe(
            map((res: HttpResponse<any>) => {
                const documents: Document[] = [];
                res.body.forEach((doc: any) =>
                    documents.push({
                        // make a copy because we must convert ISO-8601 string to Date
                        id: doc.id,
                        title: doc.title,
                        size: doc.size,
                        creator: doc.creator,
                        creationDate: new Date(doc.creationDate),
                        extension: doc.extension
                    })
                );
                return documents;
            })
        );
    }
}
