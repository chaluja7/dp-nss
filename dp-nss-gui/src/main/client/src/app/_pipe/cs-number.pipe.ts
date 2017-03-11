import {Pipe, PipeTransform} from "@angular/core";

@Pipe({name: 'csNumber'})
export class CsNumberPipe implements PipeTransform {

    transform(value: number, ...args: any[]): string {
        if(!isNaN(value)) {
            let options = new MyOptions();
            if(args && args.length > 0) {
                //1.6-6
                let arg: string = args[0];
                let split: string[] = arg.split('.');
                options.maximumFractionDigits = parseInt(split[0]);

                let split2: string[] = split[1].split('-');
                options.minimumFractionDigits = parseInt(split2[0]);
                options.maximumFractionDigits = parseInt(split2[1]);
            }

            return value.toLocaleString('cs', options);
        }

        return null;
    }

}

export class MyOptions {
    minimumFractionDigits: number;
    maximumFractionDigits: number;
    minimumIntegerDigits: number;
}