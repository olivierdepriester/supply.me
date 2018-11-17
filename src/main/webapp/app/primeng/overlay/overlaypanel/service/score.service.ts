import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Score from './score';

@Injectable()
export class ScoreService {
    constructor(private http: HttpClient) {}

    getScores(): Observable<Score[]> {
        return this.http.get<Score[]>('/content/primeng/assets/data/json/scores/scores.json');
    }
}
