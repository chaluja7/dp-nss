import {WheelChair} from "../_model/wheel-chair-model";
export class AppSettings {

    public static API_ENDPOINT = '/api/v1/';

    public static SCHEMA_URL_PREFIX = 'x-';

    public static AUTH_HEADER = 'X-Auth';

    public static OFFSET_HEADER = 'X-Offset';

    public static LIMIT_HEADER = 'X-Limit';

    public static ORDER_HEADER = 'X-Order';

    public static TOTAL_COUNT_HEADER = 'X-Total-Count';

    public static DEFAULT_PAGE_LIMIT = 20;

    public static SAVE_SUCCESS = 'Záznam byl úspěšně uložen.';

    public static DELETE_SUCCESS = 'Záznam byl úspěšně smazán.';

    public static SAVE_ERROR = 'Chyba při zpracování.';

    public static getSchemaUrlParam(schema: string) {
        return AppSettings.SCHEMA_URL_PREFIX + schema;
    }

    public static getWheelChairText(code: number): string {
        if(code === null) return null;

        switch(code) {
            case 0:
                return '?';
            case 1:
                return 'Ano';
            case 2:
                return 'Ne';
            default:
                return '?';
        }
    }

    public static getPossibleWheelChairOptions(): WheelChair[] {
        let list: WheelChair[] = [];
        list.push(new WheelChair(null, null));
        list.push(new WheelChair(0, '?'));
        list.push(new WheelChair(1, 'Ano'));
        list.push(new WheelChair(2, 'Ne'));

        return list;
    }

}