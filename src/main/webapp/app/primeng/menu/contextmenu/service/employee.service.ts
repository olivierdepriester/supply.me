import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import Employee from './employee';

@Injectable()
export class EmployeeService {
    constructor(private http: HttpClient) {}

    getEmployees(): Observable<Employee[]> {
        return this.http.get<Employee[]>('content/primeng/assets/data/json/employees/employees.json');
    }
}
