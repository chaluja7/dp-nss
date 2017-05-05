import {EnumModel} from "../_model/enum-model";

/**
 * Pomocna trida aplikace
 */
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

    public static PWD_SUCCESS = 'Heslo bylo úspěšně změněno';

    public static DELETE_SUCCESS = 'Záznam byl úspěšně smazán.';

    public static SAVE_ERROR = 'Chyba při zpracování - ';

    public static LOGOUT_SUCCESS = 'Odhlášení proběhlo úspěšně.';

    public static getSchemaUrlParam(schema: string) {
        return AppSettings.SCHEMA_URL_PREFIX + schema;
    }

    /**
     * @param code kod typu bezbarierovosti
     * @returns {string} text typu bezbarierovosti
     */
    public static getWheelChairText(code: number): string {
        if (code === null) return null;

        switch (code) {
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

    /**
     * @returns {EnumModel[]} moznosti bezbarierovosti spoje a stanice
     */
    public static getPossibleWheelChairOptions(): EnumModel[] {
        let list: EnumModel[] = [];
        list.push(new EnumModel(0, '?'));
        list.push(new EnumModel(1, 'Ano'));
        list.push(new EnumModel(2, 'Ne'));

        return list;
    }

    /**
     * @param code kod typu spoje
     * @returns {string} nazev typu spoje
     */
    public static getRouteTypeText(code: number): string {
        if (code === null) return null;

        switch (code) {
            case 0:
                return 'TRAM';
            case 1:
                return 'METRO';
            case 2:
                return 'TRAIN';
            case 3:
                return 'BUS';
            case 4:
                return 'BOAT';
            case 5:
                return 'CABLE CAR';
            case 6:
                return 'GONDOLA';
            case 7:
                return 'FUNICULAR';
            default:
                return '?';
        }
    }

    /**
     * @returns {EnumModel[]} vsechny moznosti typu spoje
     */
    public static getPossibleRouteTypesOptions(): EnumModel[] {
        let list: EnumModel[] = [];
        list.push(new EnumModel(0, 'TRAM'));
        list.push(new EnumModel(1, 'METRO'));
        list.push(new EnumModel(2, 'TRAIN'));
        list.push(new EnumModel(3, 'BUS'));
        list.push(new EnumModel(4, 'BOAT'));
        list.push(new EnumModel(5, 'CABLE CAR'));
        list.push(new EnumModel(6, 'GONDOLA'));
        list.push(new EnumModel(7, 'FUNICULAR'));

        return list;
    }

    /**
     * @param code kod typu vyjimky z intervalu platnosti
     * @returns {string} nazev typu vyjimky z intervalu platnosti
     */
    public static getExceptionTypeText(code: number): string {
        if (code === null) return null;

        switch (code) {
            case 1:
                return 'Jede';
            case 2:
                return 'Nejede';
            default:
                return '?';
        }
    }

    /**
     * @returns {EnumModel[]} vsechny moznosti typu vyjimek z intervalu platnosti
     */
    public static getPossibleExceptionTypeOptions(): EnumModel[] {
        let list: EnumModel[] = [];
        list.push(new EnumModel(1, 'Jede'));
        list.push(new EnumModel(2, 'Nejede'));

        return list;
    }

}
