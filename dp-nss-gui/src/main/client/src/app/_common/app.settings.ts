export class AppSettings {

    public static API_ENDPOINT = '/api/v1/';

    public static SCHEMA_URL_PREFIX = 'x-';

    public static AUTH_HEADER = 'X-Auth';

    public static OFFSET_HEADER = 'X-Offset';

    public static LIMIT_HEADER = 'X-Limit';

    public static ORDER_HEADER = 'X-Order';

    public static TOTAL_COUNT_HEADER = 'X-Total-Count';

    public static DEFAULT_PAGE_LIMIT = 20;

    public static getSchemaUrlParam(schema: string) {
        return AppSettings.SCHEMA_URL_PREFIX + schema;
    }

    public static getWheelChairText(code: number): string {
        if(code === null) return null;

        switch(code) {
            case 0:
                return '-';
            case 1:
                return 'Ano';
            case 2:
                return 'Ne';
            default:
                return '?';
        }
    }

}